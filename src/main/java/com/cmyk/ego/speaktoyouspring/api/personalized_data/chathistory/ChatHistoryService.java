package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoom;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoomRepository;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.ChatHistoryErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
}
