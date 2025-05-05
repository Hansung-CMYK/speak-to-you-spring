package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    // 삭제되지 않은 채팅방 내역을 페이징 처리해서 가져오는 메서드
    Page<ChatHistory> findByIsDeletedFalse(Pageable pageable);

    // 채팅방 ID, 삭제 여부, 날짜를 기준으로 조회하는 메서드
    List<ChatHistory> findByChatRoomIdAndIsDeletedFalseAndChatAtBetween(Long chatRoomId, LocalDateTime start, LocalDateTime end);
}
