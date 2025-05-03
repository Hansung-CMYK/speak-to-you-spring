package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface ChatRoomErrorCode {
    ErrorMessage ERROR_CHATROOM_NOT_FOUND = new ErrorMessage(404, "일치하는 chatroom이 존재하지 않습니다.");
    ErrorMessage ERROR_CHATROOM_NOT_EXISTS = new ErrorMessage(404, "채팅방이 존재하지 않습니다.");
}
