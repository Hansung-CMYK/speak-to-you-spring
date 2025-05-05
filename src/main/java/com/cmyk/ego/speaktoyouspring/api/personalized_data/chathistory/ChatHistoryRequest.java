package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * page단위로 채팅내역을 조회하기 위한 TDO
 * */
@Data
@Builder
public class ChatHistoryRequest {
    @NotNull(message = "uid는 필수값입니다.")
    private String uid;

    @NotNull(message = "chatRoomId는 필수값입니다.")
    private Long chatRoomId;
}