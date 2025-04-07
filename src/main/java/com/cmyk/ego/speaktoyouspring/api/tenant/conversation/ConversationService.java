package com.cmyk.ego.speaktoyouspring.api.tenant.conversation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Conversation> getConversations(Long logId) {
        List<Conversation> result = conversationRepository.findByLogIdOrderByCreatedAtDesc(logId);

        if (result.isEmpty()) {
            throw new EntityNotFoundException("해당 logId에 대한 대화가 없습니다.");
        }

        return result;
    }
}
