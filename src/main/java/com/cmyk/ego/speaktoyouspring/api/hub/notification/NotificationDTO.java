package com.cmyk.ego.speaktoyouspring.api.hub.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    @Schema(hidden = true)
    private Long notificationId;

    @Schema(example = "user_id_001")
    private String uid;

    @Schema(example = "1")
    private Long egoId;

    @Schema(example = "일기 작성 시간!")
    private String title;

    @Schema(example = "2022-05-06 12:00:00")
    private LocalDateTime createdAt;

    @Schema(example = "<html><body><h1>일기 내용입니다.</h1></body></html>")
    private String contentHtml;

    @Schema(example = "false", hidden = true)
    private Boolean isRead;

    @Schema(example = "false", hidden = true)
    private Boolean isDeleted;

    public Notification toEntity() {
        return Notification.builder()
                .notificationId(notificationId)
                .uid(uid)
                .egoId(egoId)
                .title(title)
                .createdAt(createdAt)
                .contentHtml(contentHtml)
                .isRead(isRead)
                .isDeleted(isDeleted)
                .build();
    }
}
