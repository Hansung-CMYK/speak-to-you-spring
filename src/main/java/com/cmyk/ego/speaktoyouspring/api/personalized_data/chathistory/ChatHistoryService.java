package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoom;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoomRepository;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.ChatHistoryErrorCode;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.ChatRoomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

        List<ChatHistory> chatHistories = chatHistoryRepository.findByChatRoomIdAndIsDeletedFalseAndChatAtBetween(chatRoomId, startOfDay, endOfDay);

        // 정렬 (오래된 순으로 정렬)
        chatHistories.sort(Comparator.comparing(ChatHistory::getChatAt));

        return chatHistories;
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

    public static String getSHA256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

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
}
