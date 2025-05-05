package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom;

import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.ChatRoomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        return chatRoomRepository.save(foundChatRoom);
    }

    public Page<ChatRoom> getChatRooms(int pageNum, int pageSize) {

        // Pageable 객체를 생성 (from, to는 페이지 번호 기준으로 0부터 시작)
        // from: 페이지 번호, to: 페이지 크기
        // 하나의 페이지에 몇개의 데이터가 들어갈지 : pageSize
        // pageSize로 나뉘어진 page에서 몇번째 page를 조회할까요 : pageNum
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastChatAt")));

        return chatRoomRepository.findByIsDeletedFalse(pageable);
    }
}
