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
     */
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

    /**
     * 채팅방 삭제
     * 필수값 : uid, egoid
     */
    @DeleteMapping("delete")
    public ResponseEntity delete(@RequestBody ChatRoomDTO targetChatRoom) {
        if (targetChatRoom.getUid() == null || targetChatRoom.getEgoId() == null) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: uid와 egoid는 필수 값입니다.")
                    .build());
        }

        TenantContext.setCurrentTenant(targetChatRoom.getUid());

        var result = chatRoomService.delete(targetChatRoom);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("채팅방 삭제 완료").data(result).build());
    }

    /**
     * page수와 pagesize에 따른 채팅방 리스트 조회
     * */
    @PostMapping("search")
    public ResponseEntity getUndeletedChatRooms(@RequestBody @Valid ChatRoomSearchRequest chatRoomSearchRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        TenantContext.setCurrentTenant(chatRoomSearchRequest.getUid());

        var result = chatRoomService.getChatRooms(chatRoomSearchRequest);

        int pageNum = chatRoomSearchRequest.getPageNum();
        int pageSize = chatRoomSearchRequest.getPageSize();

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message(String.format("채팅방 조회 pagenum_%d, pagesize_%d", pageNum, pageSize))
                .data(result.getContent())
                .build());
    }
}
