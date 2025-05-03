package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * page단위로 채팅방을 조회하기 위한 TDO
 * */
@Data
@Builder
public class ChatRoomSearchRequest {
    @NotNull(message = "uid는 필수값입니다.")
    private String uid;

    @NotNull(message = "page 값은 필수값입니다.")
    @Min(value = 0, message = "from 값은 0 이상이어야 합니다.")
    private Integer pageNum;

    @NotNull(message = "pageSize 값은 필수값입니다.")
    @Min(value = 1, message = "to 값은 1 이상이어야 합니다.")
    private Integer pageSize;
}