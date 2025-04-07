package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationLogRepository extends JpaRepository<ConversationLog, Long> {

    // status가 'A'인것중에서 가장 큰 orderIndex 반환
    @Query("SELECT COALESCE(MAX(c.orderIndex), 0) FROM ConversationLog c WHERE c.status = 'A'")
    int findMaxOrderIndex();

    List<ConversationLog> findAllByStatus(String status);

    ConversationLog findByLogId(Long LogId);
}
