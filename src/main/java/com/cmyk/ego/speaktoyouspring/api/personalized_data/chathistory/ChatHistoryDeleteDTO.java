package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatHistoryDeleteDTO {
    @Schema(example = "1")
    @NotBlank(message = "채팅 메세지 ID는 필수입니다.")
    private Long chatHistoryId;

    @Schema(example = "user_id_001")
    @NotBlank(message = "UID는 필수입니다.")
    private String uid;
}
