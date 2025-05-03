package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
}
