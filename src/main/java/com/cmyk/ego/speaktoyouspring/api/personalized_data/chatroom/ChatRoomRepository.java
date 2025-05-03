package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    Optional<ChatRoom> findByUidAndEgoIdAndIsDeletedFalse(String uid, Integer egoId);

    // 삭제되지 않은 채팅방 리스트를 페이징 처리해서 가져오는 메서드
    Page<ChatRoom> findByIsDeletedFalse(Pageable pageable);

    // chatRoomId가 같은 것을 찾아주는 메서드
    Optional<ChatRoom> findByIdAndIsDeletedFalse(Integer id);
}
