package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

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

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat-history")
@RequiredArgsConstructor
public class ChatHistoryController {
    private final ChatHistoryService chatHistoryService;
    private final UserAccountRepository userAccountRepository;

    /**
     * 채팅 내역 생성
     */
    @Operation(summary = "채팅 메세지 생성하는 API(에고와 사람 채팅방 용도)", description = "단일 채팅 메세지를 생성한다.<br> type: U(사용자) E(에고) 가 보낸 채팅<br>contentType: TEXT(텍스트 채팅) IMAGE(이미지 채팅)", tags = {"채팅 기록"})
    @PostMapping
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

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(chatHistoryDTO.getUid()).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(chatHistoryDTO.getUid());

        var result = chatHistoryService.create(chatHistoryDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 생성 완료").data(result).build());
    }

    /**
     * page수와 pagesize에 따른 채팅 내역 조회
     * 필수값 : uid, chatRoomId
     */
    @Operation(summary = "사용자id과 roomId로 채팅 메세지 리스트를 반환하는 API", description = "사용자 id와 roomId로 해당 채팅 메세지 리스트를 조회하는 API<br>최신 순으로 가져온다.<br>pageNum: 최신 순으로 정렬한 해당 방의 메세지의 인덱스 번호<br>pageSize: 한 번에 가져올 메세지 리스트의 크기", tags = {"채팅 기록"})
    @PostMapping("/list")
    public ResponseEntity getUndeletedChatHistories(
            @RequestBody @Valid ChatHistoryRequest chatHistoryRequest,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        if (pageNum < 0 || pageSize < 1) {
            return ResponseEntity.badRequest()
                    .body(CommonResponse.builder()
                            .code(400)
                            .message("pageNum은 0이상, pageSize는 1이상이여야 합니다.")
                            .build());
        }

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(chatHistoryRequest.getUid()).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(chatHistoryRequest.getUid());

        var result = chatHistoryService.getPagedChatHistories(chatHistoryRequest.getChatRoomId(), pageNum, pageSize);

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message(String.format("채팅내역 조회 pagenum_%d, pagesize_%d", pageNum, pageSize))
                .data(result.getContent())
                .build());
    }

    /**
     * 전달받은 날짜에 해당하는 하루치 채팅내역조회
     */
    @Operation(summary = "특정 날짜의 사용자의 채팅방의 기록 가져오는 API", description = "yyyy-MM-dd 포맷의 날짜를 입력하면, 사용자Id와 해당하는 chatRoomId의 채팅 기록을 리스트로 반환한다.<br>리스트 메세지는 오래된 순으로 정렬한다.", tags = {"채팅 기록"})
    @PostMapping("/daily")
    public ResponseEntity getUndeletedChatHistories(
            @RequestBody @Valid ChatHistoryRequest chatHistoryRequest,
            @RequestParam("date") String dateString,
            BindingResult bindingResult) {

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
        userAccountRepository.findByUid(chatHistoryRequest.getUid()).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(chatHistoryRequest.getUid());

        var result = chatHistoryService.getDailyChatHistories(chatHistoryRequest.getChatRoomId(), dateString);

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message(String.format("%s 의 채팅내역 조회", dateString))
                .data(result)
                .build());
    }

    /**
     * 채팅내역 삭제
     * 필수값 : uid, id
     */
    @Operation(summary = "사용자 id와 채팅 메세지 id로 특정 메세지 삭제하는 API", description = "사용자 id와 채팅 메시지 id로 삭제한다. <i>실제 삭제가 아닌 is_deleted 값 변경.</i>", tags = {"채팅 기록"})
    @DeleteMapping
    public ResponseEntity delete(@RequestBody ChatHistoryDeleteDTO chatHistoryDTO) {

        if (chatHistoryDTO.getUid() == null || chatHistoryDTO.getChatHistoryId() == null) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: uid와 chatHistoryId 필수 값입니다.")
                    .build());
        }

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(chatHistoryDTO.getUid()).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(chatHistoryDTO.getUid());

        var result = chatHistoryService.deleteChatHistory(chatHistoryDTO.getChatHistoryId());

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 삭제 완료").data(result).build());
    }

    /**
     * 채팅내역 해시값으로 삭제
     * 필수값 : uid, hash
     */
    @Operation(summary = "해시값으로 특정 채팅 메세지 이후의 메세지들을 전부 삭제하는 API", description = "특정 메세지를 삭제하면 채팅 기록상 이후의 것들도 한꺼번에 삭제된다. <i>실제 삭제가 아닌 is_deleted 값 변경.</i>", tags = {"채팅 기록"})
    @DeleteMapping("/{userid}/{hash}")
    public ResponseEntity deleteByHash(@PathVariable("userid") String userid, @PathVariable("hash") String hash) {
        if (userid == null || hash == null) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: userid와 hash는 필수 값입니다.")
                    .build());
        }

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(userid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(userid);

        var result = chatHistoryService.deleteChatHistoryByHash(hash);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 해시값으로 삭제 완료").data(result).build());
    }

    @Operation(summary = "유저의 채팅내역 조회", description = "uid와 날짜를 입력받아 해당 날짜의 채팅내역을 조회합니다. <br> example: uid: user_id_001 / user_id_002 <br> datetime: 2025-05-19", tags = {"채팅 기록"})
    @GetMapping("/{uid}/{datetime}")
    public ResponseEntity getChatHistoriesByUid(@PathVariable(value = "uid") String userid, @PathVariable("datetime") String datetime) throws IOException, ExecutionException, InterruptedException {

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(userid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(userid);

        if (datetime == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 일자 형식입니다. yyyy-MM-dd로 입력해주세요.");
        }

        var result = chatHistoryService.getChatHistoryByUidAndDate(userid, datetime);

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message(String.format("%s일 에고채팅, 사람채팅 내역 전체 조회", datetime))
                .data(result)
                .build());
    }

}
