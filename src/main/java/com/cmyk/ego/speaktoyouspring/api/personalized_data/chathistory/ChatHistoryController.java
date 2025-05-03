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

    /**
     * page수와 pagesize에 따른 전체 채팅내역 조회
     */
    @PostMapping("/paged")
    public ResponseEntity getUndeletedChatHistories(
            @RequestBody @Valid ChatHistoryRequest chatHistoryRequest,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        if (pageNum<0 || pageSize<1) {
            return ResponseEntity.badRequest()
                    .body(CommonResponse.builder()
                            .code(400)
                            .message("pageNum은 0이상, pageSize는 1이상이여야 합니다.")
                            .build());
        }

        TenantContext.setCurrentTenant(chatHistoryRequest.getUid());

        var result = chatHistoryService.getPagedChatHistories(pageNum, pageSize);

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message(String.format("채팅내역 조회 pagenum_%d, pagesize_%d", pageNum, pageSize))
                .data(result.getContent())
                .build());
    }

    /**
     * 전달받은 날짜에 해당하는 하루치 채팅내역조회
     */
    @PostMapping("daily")
    public ResponseEntity getUndeletedChatHistories(
            @RequestBody @Valid ChatHistoryRequest chatHistoryRequest,
            @RequestParam("date") String dateString,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        TenantContext.setCurrentTenant(chatHistoryRequest.getUid());

        var result = chatHistoryService.getDailyChatHistories(chatHistoryRequest.getChatRoomId(), dateString);

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message(String.format("%s일자의 채팅내역 조회", dateString))
                .data(result)
                .build());
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
