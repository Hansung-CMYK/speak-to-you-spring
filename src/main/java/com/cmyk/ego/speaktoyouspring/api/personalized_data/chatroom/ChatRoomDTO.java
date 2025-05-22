package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    @Schema(hidden = true)
    private Long id; // 채팅방 ID

    @Schema(example = "user_id_001")
    @NotBlank(message = "UID는 필수입니다.")
    private String uid; // 사용자 UID

    @Schema(example = "1")
    @NotNull(message = "EGO ID는 필수입니다.")
    private Integer egoId; // EGO ID

    @Schema(example = "2025-05-19 23:30:25.998")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastChatAt; // 최근 대화 시간

    @Schema(example = "false", hidden = true)
    private Boolean isDeleted; // 삭제 여부

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .id(id)
                .uid(uid)
                .egoId(egoId)
                .lastChatAt(lastChatAt)
                .isDeleted(isDeleted)
                .build();
    }
}
