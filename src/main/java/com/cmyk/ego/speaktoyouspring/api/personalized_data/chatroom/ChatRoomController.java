package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom;

import com.cmyk.ego.speaktoyouspring.api.hub.ego.EgoService;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final EgoService egoService;
    private final UserAccountRepository userAccountRepository;

    /**
     * 채팅방 생성
     * 필수값 : uid, egoid
     */
    @Operation(summary = "사용자 id와 에고 id로 채팅방을 생성/수정하는 API", description = "uid와 egoid를 입력받아 해당 채팅방을 생성합니다.<br>단, 기존에 uid-egoId로 생성된 채팅방이 있는 경우, lastChatAt이 업데이트 됩니다.<br>lastChatAt을 전달하지 않으면, 현재 시각으로 전달됩니다. <br> lastChatAt(필수 아님)", tags = {"채팅방"})
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
    @Operation(summary = "사용자 id와 에고 id로 채팅방을 삭제하는 API", description = "사용자 ID와 에고 ID로 채팅방을 삭제한다. <i>실제 삭제가 아닌 is_deleted 값 변경.</i>", tags = {"채팅방"})
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
    @Operation(summary = "사용자id로 채팅방 리스트를 조회하는 API", description = "사용자 id로 채팅방을 조회한다. <br>리스트 순서는 lastChatAt을 기준으로 최신순으로 정렬된다.", tags = {"채팅방"})
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

    /**
     * 채팅방 단일 조회
     */
    @Operation(summary = "사용자 ID, 에고 ID로 채팅방 ID 조회하는 API", description = "사용자 ID와 에고 ID로 채팅방 ID를 조회한다.", tags = {"채팅방"})
    @GetMapping("{userId}/{egoId}")
    public ResponseEntity getChatRoom(@PathVariable("userId") String userId, @PathVariable("egoId") Integer egoId) {
        Map<String, Long> result = new HashMap<>();
        result.put("chatRoomId", chatRoomService.getChatRoomId(userId, egoId));


        return ResponseEntity.ok(CommonResponse.builder().code(200).message("채팅방 ID 조회 완료").data(result).build());
    }
}
