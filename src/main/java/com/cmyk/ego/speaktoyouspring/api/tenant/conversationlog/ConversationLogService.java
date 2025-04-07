package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationLogService {
    private final ConversationLogRepository conversationLogRepository;

    public ConversationLog create(ConversationLog conversationLog) {
        int nextOrderIndex = conversationLogRepository.findMaxOrderIndex() + 1;

        return conversationLogRepository.save(
                ConversationLog.builder()
                        .topic(conversationLog.getTopic())
                        .lastQuestion(conversationLog.getLastQuestion())
                        .orderIndex(nextOrderIndex)
                        .build()
        );
    }

    public List<ConversationLog> readAll() {
        return conversationLogRepository.findAll();
    }

    public List<ConversationLog> readAllActiveLogs() {
        return conversationLogRepository.findAllByStatus("A");
    }
}
