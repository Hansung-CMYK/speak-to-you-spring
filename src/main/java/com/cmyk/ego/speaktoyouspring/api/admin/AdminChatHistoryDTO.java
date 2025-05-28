package com.cmyk.ego.speaktoyouspring.api.admin;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminChatHistoryDTO {
    private String content;
    private String type;
    private LocalDateTime chatAt;
    private String contentType = "TEXT";
}
