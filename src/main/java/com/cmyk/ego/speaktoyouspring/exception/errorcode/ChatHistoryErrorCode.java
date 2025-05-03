package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface ChatHistoryErrorCode {
    ErrorMessage ERROR_CHATROOM_NOT_EXISTS = new ErrorMessage(404, "chatRoomId를 가지는 채팅방이 없습니다.");
}
