package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatRoom;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room") // chat_room 테이블과 매핑
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // 채팅방 ID
    private Integer id;

    @Column(name = "uid", nullable = false)
    private String uid; // 사용자 UID

    @Column(name = "ego_id", nullable = false)
    private Integer egoId;

    @Builder.Default
    @Column(name = "last_chat_at")
    private LocalDateTime lastChatAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
