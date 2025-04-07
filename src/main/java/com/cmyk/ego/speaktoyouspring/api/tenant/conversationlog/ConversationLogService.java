package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationLogService {
    private final ConversationLogRepository conversationLogRepository;

    public ConversationLog create(ConversationLog conversationLog) {
        return conversationLogRepository.save(
                ConversationLog.builder()
                        .logId(conversationLog.getLogId())
                        .topic(conversationLog.getTopic())
                        .build()
        );
    }

    public List<ConversationLog> readAll() {
        return conversationLogRepository.findAll();
    }
}
