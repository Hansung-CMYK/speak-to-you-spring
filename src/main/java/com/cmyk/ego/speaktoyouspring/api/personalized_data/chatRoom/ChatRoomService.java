package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatRoom;

import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.ChatRoomErrorCode;
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

    public ChatRoom delete(ChatRoomDTO targetChatRoom){

        var uid = targetChatRoom.getUid();
        var egoId = targetChatRoom.getEgoId();

        // uid와 egoid가 일치하면서 is_deleted가 false인 값 조회
        var foundChatRoom = chatRoomRepository.findByUidAndEgoIdAndIsDeletedFalse(uid, egoId)
                .orElseThrow(() -> new ControlledException(ChatRoomErrorCode.ERROR_CHATROOM_NOT_FOUND));

        // 값을 직접 삭제 하지 않고 is_deleted속성을 true로 변경
        foundChatRoom.setIsDeleted(true);

        return foundChatRoom;
    }
}
