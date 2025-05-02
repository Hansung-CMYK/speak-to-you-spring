package com.cmyk.ego.speaktoyouspring.api.personalized_data.conversation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public Conversation create(Conversation conversation) {
        return conversationRepository.save(
                Conversation.builder()
                        .logId(conversation.getLogId())
                        .content(conversation.getContent())
                        .build()
        );
    }
}
