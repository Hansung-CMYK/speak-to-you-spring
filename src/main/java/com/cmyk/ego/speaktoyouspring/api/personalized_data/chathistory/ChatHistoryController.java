package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
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
                .message(String.format("%s일자의 채팅내역 조회", dateString))
                .data(result)
                .build());
    }

    /**
     * 채팅내역 삭제
     * 필수값 : uid, id
     */
    @DeleteMapping
    public ResponseEntity delete(@RequestBody ChatHistoryDTO chatHistoryDTO) {

        if (chatHistoryDTO.getUid() == null || chatHistoryDTO.getId() == null) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: uid와 id는 필수 값입니다.")
                    .build());
        }

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(chatHistoryDTO.getUid()).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(chatHistoryDTO.getUid());

        var result = chatHistoryService.deleteChatHistory(chatHistoryDTO.getId());

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 삭제 완료").data(result).build());
    }

    /**
     * 채팅내역 해시값으로 삭제
     * 필수값 : uid, hash
     */
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

    @Operation(summary = "유저의 채팅내역 조회", description = "uid와 날짜를 입력받아 해당 날짜의 채팅내역을 조회합니다. <br> example: uid: user_id_001 / user_id_002 <br> datetime: 2025-05-19")
    @GetMapping("/{uid}/{datetime}")
    public ResponseEntity getChatHistoriesByUid(@PathVariable(value = "uid") String userid, @PathVariable("datetime") String datetime) throws IOException, ExecutionException, InterruptedException {

        // 작성을 원하는 ~ 날짜의 문자열을 Date 객체로 변환 (형식: yyyy-MM-dd)
        Date targetDate = parseDate(datetime);
        if (targetDate == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 일자 형식입니다. yyyy-MM-dd로 입력해주세요.");
        }

        Firestore db = FirestoreClient.getFirestore();
        List<Map<String, Object>> result = new ArrayList<>();

        // chats/user_chat/ 하위 [컬렉션]들 조회 (ex. user1_user2, user3_user1 등)
        for (CollectionReference chatCollection : db.collection("chats").document("user_chat").listCollections()) {
            // 해당 컬렉션명이 요청한 사용자 ID를 포함하는 경우만 처리
            if (!chatCollection.getId().contains(userid)) continue;

            // 필드 중 timestamp를 기준으로 오름차순으로 채팅 메세지 [문서]들 정렬
            List<QueryDocumentSnapshot> documents = chatCollection
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get().get().getDocuments();

            // 채팅 메세지 [문서] 하나씩 순회하면서 요청한 날짜 필터링 및 결과 리스트에 추가
            for (QueryDocumentSnapshot doc : documents) {
                Timestamp timestamp = doc.getTimestamp("timestamp");

                // timestamp가 없거나 날짜가 일치하지 않으면 skip
                if (timestamp == null || !isSameDay(timestamp.toDate(), targetDate)) continue;

                // 결과 리스트에 담을 필드만 추출하여 Map 생성
                result.add(Map.of(
                        "uid", Objects.requireNonNull(doc.getString("sender_id")), // 보낸 사람 UID
                        "type", Objects.requireNonNull(doc.getString("sender_id")).equals(userid) ? "U" : "E",
                        "content", Objects.requireNonNull(doc.getString("text")), // 채팅 내용
                        "chat_at", formatDate(timestamp.toDate()) // 보낸 날짜 (yyyy-MM-dd)
                ));
            }
        }

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message(String.format("%s일자의 채팅내역 조회", datetime))
                .data(result)
                .build());
    }

    // 날짜 파싱 (yyyy-MM-dd)
    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    // Date → yyyy-MM-dd 문자열
    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    // 두 날짜가 같은 일인지 비교
    private boolean isSameDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

}
