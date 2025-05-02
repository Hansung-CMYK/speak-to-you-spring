package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    Optional<ChatRoom> findByUidAndEgoIdAndIsDeletedFalse(String uid, Integer egoId);
}
