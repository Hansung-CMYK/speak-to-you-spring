package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_log") // 테이블 이름
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConversationLog {
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversation_log_seq")
    @SequenceGenerator(name = "conversation_log_seq", sequenceName = "conversation_log_seq", allocationSize = 1)
    private Long logId;

    @Column(name = "topic")
    private String topic;

    @Column(name = "last_question")
    private String lastQuestion;

    @Column(name = "order_index")
    private int orderIndex;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private String status = "A";
}
