package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom create(ChatRoomDTO chatRoomDTO){

        if (chatRoomDTO.getLastChatAt() == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String formattedDate = LocalDateTime.now().format(formatter);
            chatRoomDTO.setLastChatAt(LocalDateTime.parse(formattedDate, formatter));
        }

        if (chatRoomDTO.getIsDeleted() == null){
            chatRoomDTO.setIsDeleted(false);
        }

        return chatRoomRepository.save(chatRoomDTO.toEntity());
    }
}
