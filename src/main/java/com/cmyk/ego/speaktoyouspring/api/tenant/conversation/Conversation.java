package com.cmyk.ego.speaktoyouspring.api.tenant.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/// tenant.{schemaName}.talk 테이블 정보가 명시된 클래스이다.
@Entity
@Table(name = "conversation") // 테이블 이름
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Conversation {
    @Id
    @Column(name = "conversation_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversation_seq")
    @SequenceGenerator(name = "conversation_seq", sequenceName = "conversation_seq", allocationSize = 1)
    private Long conversationId;

    @Column(name = "log_id")
    private Long logId;

    @Column(name = "content")
    private String content;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "type")
    private String type;
}