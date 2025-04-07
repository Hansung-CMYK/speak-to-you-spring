package com.cmyk.ego.speaktoyouspring.api.tenant.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // 최근 날짜순으로 정렬 (2024.02.05~2024.02.01)
    List<Conversation> findByLogIdOrderByCreatedAtDesc(Long logId);

}
