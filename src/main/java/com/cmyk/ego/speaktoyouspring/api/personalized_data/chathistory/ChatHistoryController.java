package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat-history/")
@RequiredArgsConstructor
public class ChatHistoryController {
    private final ChatHistoryService chatHistoryService;

    @PostMapping("create")
    public ResponseEntity create(@RequestBody @Valid ChatHistoryDTO chatHistoryDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        TenantContext.setCurrentTenant(chatHistoryDTO.getUid());

        var result = chatHistoryService.create(chatHistoryDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 생성 완료").data(result).build());
    }

    @DeleteMapping("delete")
    public ResponseEntity delete(@RequestBody ChatHistoryDTO chatHistoryDTO) {

        if (chatHistoryDTO.getUid() == null || chatHistoryDTO.getId() == null) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: uid와 id는 필수 값입니다.")
                    .build());
        }

        TenantContext.setCurrentTenant(chatHistoryDTO.getUid());

        var result = chatHistoryService.deleteChatHistory(chatHistoryDTO.getId());

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 삭제 완료").data(result).build());
    }

}
