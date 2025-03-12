package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
