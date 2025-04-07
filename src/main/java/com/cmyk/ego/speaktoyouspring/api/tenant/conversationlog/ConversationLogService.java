package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import jakarta.persistence.EntityNotFoundException;
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

    public ConversationLog delete(Long logId) {
        var target = conversationLogRepository.findByLogId(logId);

        // logId를 가지는 ConversationLog 데이터가 없는 경우
        if (target == null) {
            throw new EntityNotFoundException("해당 logId의 데이터가 존재하지 않습니다.");
        }

        target.setStatus("D"); // status의 값을 'D'로 변경
        target.setOrderIndex(0); // orderIndex의 값을 0으로 설정
        return conversationLogRepository.save(target);
    }
}
