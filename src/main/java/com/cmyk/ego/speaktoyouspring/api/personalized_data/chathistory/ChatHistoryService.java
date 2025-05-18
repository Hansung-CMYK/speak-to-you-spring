package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoom;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoomRepository;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.ChatHistoryErrorCode;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.ChatRoomErrorCode;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatHistory create(ChatHistoryDTO chatHistoryDTO){

        if(chatHistoryDTO.getIsDeleted() == null){
            chatHistoryDTO.setIsDeleted(false);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDate = LocalDateTime.now().format(formatter);

        if (chatHistoryDTO.getChatAt() == null) {
            chatHistoryDTO.setChatAt(LocalDateTime.parse(formattedDate, formatter));
        }

        // 메시지 해시값 생성
        if (chatHistoryDTO.getMessageHash() == null) {
            String hash = getSHA256Hash(chatHistoryDTO.getUid() + "|" + LocalDateTime.parse(formattedDate, formatter) + "|" + chatHistoryDTO.getContent());
            chatHistoryDTO.setMessageHash(hash);
        }

        // 채팅방의 시간도 업데이트 해야함 (최신 채팅순 정렬을 위함)
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByIdAndIsDeletedFalse(chatHistoryDTO.getChatRoomId());

        // 채팅방이 존재하면, 마지막 채팅 시간을 업데이트
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            chatRoom.setLastChatAt(LocalDateTime.parse(formattedDate, formatter));

            // 채팅방 정보를 저장 (업데이트)
            chatRoomRepository.save(chatRoom);
        } else {
            throw new ControlledException(ChatHistoryErrorCode.ERROR_CHATROOM_NOT_EXISTS);
        }

        return chatHistoryRepository.save(chatHistoryDTO.toEntity());

    }

    /**
     * 전체 채팅 내역을 page단위로 조회
     * */
    public Page<ChatHistory> getPagedChatHistories(int pageNum, int pageSize) {

        // Pageable 객체를 생성 (from, to는 페이지 번호 기준으로 0부터 시작)
        // from: 페이지 번호, to: 페이지 크기
        // 하나의 페이지에 몇개의 데이터가 들어갈지 : pageSize
        // pageSize로 나뉘어진 page에서 몇번째 page를 조회할까요 : pageNum
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("chatAt")));

        return chatHistoryRepository.findByIsDeletedFalse(pageable);
    }

    /**
     * 하루치 채팅 내역 조회
     * */
    public List<ChatHistory> getDailyChatHistories(Long chatRoomId, String dateString) {
        List<LocalDateTime> dayList = convertStringToDayList(dateString);

        List<ChatHistory> chatHistories = chatHistoryRepository.findByChatRoomIdAndIsDeletedFalseAndChatAtBetween(chatRoomId, dayList.getFirst(), dayList.getLast());

        // 정렬 (오래된 순으로 정렬)
        chatHistories.sort(Comparator.comparing(ChatHistory::getChatAt));

        return chatHistories;
    }

    private List<LocalDateTime> convertStringToDayList(String dateString) {
        // 날짜 포맷 검증
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date;

        try {
            date = LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            throw new ControlledException(ChatRoomErrorCode.ERROR_DATE_FORMAT);
        }

        // 하루의 시작과 끝 시간 정의
        LocalDateTime startOfDay = date.atStartOfDay();// 00:00:00
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();// 다음날 00:00:00

        return Arrays.asList(startOfDay, endOfDay);
    }

    public ChatHistory deleteChatHistory(Long chatHistoryId){
        Optional<ChatHistory> chatHistoryOptional = chatHistoryRepository.findById(chatHistoryId);

        if(chatHistoryOptional.isEmpty()){
            throw new ControlledException(ChatHistoryErrorCode.ERROR_CHATHISTORY_NOT_EXISTS);
        }

        ChatHistory foundChatHistory =  chatHistoryOptional.get();

        foundChatHistory.setIsDeleted(true);

        chatHistoryRepository.save(foundChatHistory);

        return foundChatHistory;
    }

    public ChatHistory deleteChatHistoryByHash(String hash) {
        ChatHistory foundChatHistory = chatHistoryRepository.findByMessageHashAndIsDeletedFalse(hash)
                .orElseThrow(() -> new ControlledException(ChatHistoryErrorCode.ERROR_CHATHISTORY_NOT_EXISTS));

        foundChatHistory.setIsDeleted(true);
        return chatHistoryRepository.save(foundChatHistory);
    }

    // 문자열 기반으로 SHA-256 코드를 만드는 메서드
    public static String getSHA256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // 바이트 값에 따라서 Hex 문자열로 변환하는 메서드
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    ///
    public List<List<Map<String, Object>>> getChatHistoryByChatRoomId(String userid, String datetime) {
        List<List<Map<String, Object>>> list = new ArrayList<>();
        List<Map<String, Object>> userChatList = getFirestoreChatHistory(userid, datetime);
        list.add(userChatList);

        List<LocalDateTime> dayList = convertStringToDayList(datetime);
        List<Map<String, Object>> egoChatList = new ArrayList<>();
        chatHistoryRepository.findByChatAtBetweenAndIsDeletedFalse(dayList.getFirst(), dayList.getLast()).forEach(chatHistory -> {
            Map<String, Object> chatHistoryMap = Map.of(
                    "uid", chatHistory.getUid(),
                    "type", chatHistory.getType(),
                    "content", chatHistory.getContent(),
                    "chat_at", formatDate(java.sql.Timestamp.valueOf(chatHistory.getChatAt()))
            );

            egoChatList.add(chatHistoryMap);
        });
        list.add(egoChatList);

        return list;
    }

    private List<Map<String, Object>> getFirestoreChatHistory(String userid, String datetime){

        // 작성을 원하는 ~ 날짜의 문자열을 Date 객체로 변환 (형식: yyyy-MM-dd)
        Date targetDate = parseDate(datetime);

        Firestore db = FirestoreClient.getFirestore();
        List<Map<String, Object>> result = new ArrayList<>();

        // chats/user_chat/ 하위 [컬렉션]들 조회 (ex. user1_user2, user3_user1 등)
        for (CollectionReference chatCollection : db.collection("chats").document("user_chat").listCollections()) {
            // 해당 컬렉션명이 요청한 사용자 ID를 포함하는 경우만 처리
            if (!chatCollection.getId().contains(userid)) continue;

            try {
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
                        "type", Objects.requireNonNull(doc.getString("sender_id")).equals(userid) ? "U" : "O",
                        "content", Objects.requireNonNull(doc.getString("text")), // 채팅 내용
                        "chat_at", formatDate(timestamp.toDate()) // 보낸 날짜 (yyyy-MM-dd)
                ));
            }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return result;
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
