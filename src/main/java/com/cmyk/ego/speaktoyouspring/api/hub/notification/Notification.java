package com.cmyk.ego.speaktoyouspring.api.hub.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification") // 테이블 이름
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "notification_id", nullable = false, unique = true)
    private Long notificationId;

    @Column(name = "uid", nullable = false)
    private String uid; // 사용자 고유 ID

    @Column(name = "ego_id")
    private Long egoId;

    @Column(name = "title")
    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_deleted")
    private Boolean isDeleted;;
}
