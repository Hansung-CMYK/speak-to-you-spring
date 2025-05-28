package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryDTO {

    @Schema(hidden = true)
    private Long id;

    @Schema(example = "user_id_001")
    @NotBlank(message = "UID는 필수입니다.")
    private String uid;

    @Schema(example = "1")
    @NotNull(message = "ChatRoomId는 필수입니다.")
    private Long chatRoomId;

    private String content;

    @Pattern(regexp = "U|E", message = "type은 'U' 또는 'E'만 가능합니다.(User, EGO)")
    private String type;

    private LocalDateTime chatAt;

    @Schema(example = "false", hidden = true)
    private Boolean isDeleted = false;

    private String messageHash;

    @Schema(example = "TEXT", description = "content 타입 (TEXT, IMAGE)")
    @Pattern(regexp = "TEXT|IMAGE", message = "contentType는 'TEXT' 또는 'IMAGE'만 가능합니다.")
    private String contentType = "TEXT";

    public ChatHistory toEntity() {
        return ChatHistory.builder()
                .uid(uid)
                .chatRoomId(chatRoomId)
                .content(content)
                .type(type)
                .chatAt(chatAt)
                .isDeleted(isDeleted)
                .messageHash(messageHash)
                .contentType(contentType)
                .build();
    }
}