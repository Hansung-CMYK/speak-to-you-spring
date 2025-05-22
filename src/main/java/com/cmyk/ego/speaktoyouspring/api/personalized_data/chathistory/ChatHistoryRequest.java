package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * page단위로 채팅내역을 조회하기 위한 TDO
 * */
@Data
@Builder
public class ChatHistoryRequest {
    @Schema(example = "user_id_001")
    @NotNull(message = "uid는 필수값입니다.")
    private String uid;

    @Schema(example = "1")
    @NotNull(message = "chatRoomId는 필수값입니다.")
    private Long chatRoomId;
}