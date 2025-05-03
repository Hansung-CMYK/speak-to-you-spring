package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * page단위로 채팅방을 조회하기 위한 TDO
 * */
@Data
@Builder
public class ChatRoomPageRequest {
    @NotNull(message = "uid는 필수값입니다.")
    private String uid;
}