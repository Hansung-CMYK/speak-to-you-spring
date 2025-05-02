package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatRoom;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat-room/")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 생성
     * 필수값 : uid, egoid
     * */
    @PostMapping("create")
    public ResponseEntity create(@RequestBody @Valid ChatRoomDTO chatRoomDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        TenantContext.setCurrentTenant(chatRoomDTO.getUid());

        var result = chatRoomService.create(chatRoomDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("채팅방 생성 완료").data(result).build());
    }
}
