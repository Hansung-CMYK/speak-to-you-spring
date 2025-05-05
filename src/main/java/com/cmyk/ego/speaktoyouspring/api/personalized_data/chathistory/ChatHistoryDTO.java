package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class ChatHistoryDTO {

    private Long id;

    @NotBlank(message = "UID는 필수입니다.")
    private String uid;

    @NotNull(message = "ChatRoomId는 필수입니다.")
    private Long chatRoomId;

    private String content;

    @Pattern(regexp = "U|E", message = "type은 'U' 또는 'E'만 가능합니다.(User, EGO)")
    private String type;

    private LocalDateTime chatAt;

    private Boolean isDeleted;

    public ChatHistory toEntity() {
        return ChatHistory.builder()
                .uid(uid)
                .chatRoomId(chatRoomId)
                .content(content)
                .type(type)
                .chatAt(chatAt)
                .isDeleted(isDeleted)
                .build();
    }
}