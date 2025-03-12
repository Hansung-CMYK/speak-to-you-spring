package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationLogRepository extends JpaRepository<ConversationLog, Long> {
}
