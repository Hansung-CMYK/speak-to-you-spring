package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatRoom;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer id; // 채팅방 ID

    @NotBlank(message = "UID는 필수입니다.")
    private String uid; // 사용자 UID

    @NotNull(message = "EGO ID는 필수입니다.")
    private Integer egoId; // EGO ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastChatAt; // 최근 대화 시간

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
