package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserAccountRepository userAccountRepository;

    /**
     * 채팅방 생성
     * 필수값 : uid, egoid
     */
    @PostMapping
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

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(chatRoomDTO.getUid()).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(chatRoomDTO.getUid());

        var result = chatRoomService.create(chatRoomDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("채팅방 생성 완료").data(result).build());
    }

    /**
     * 채팅방 삭제
     * 필수값 : uid, egoid
     */
    @DeleteMapping
    public ResponseEntity delete(@RequestBody ChatRoomDTO targetChatRoom) {
        if (targetChatRoom.getUid() == null || targetChatRoom.getEgoId() == null) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: uid와 egoid는 필수 값입니다.")
                    .build());
        }

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(targetChatRoom.getUid()).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(targetChatRoom.getUid());

        var result = chatRoomService.delete(targetChatRoom);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("채팅방 삭제 완료").data(result).build());
    }

    /**
     * page수와 pagesize에 따른 채팅방 리스트 조회
     * 필수값 : uid
     */
    @PostMapping("/list")
    public ResponseEntity getUndeletedChatRooms(
            @RequestBody @Valid ChatRoomPageRequest chatRoomPageRequest,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        if (pageNum<0 || pageSize<1) {
            return ResponseEntity.badRequest()
                    .body(CommonResponse.builder()
                            .code(400)
                            .message("pageNum은 0이상, pageSize는 1이상이여야 합니다.")
                            .build());
        }

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(chatRoomPageRequest.getUid()).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(chatRoomPageRequest.getUid());

        var result = chatRoomService.getChatRooms(pageNum, pageSize);

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message(String.format("채팅방 조회 pagenum_%d, pagesize_%d", pageNum, pageSize))
                .data(result.getContent())
                .build());
    }
}
