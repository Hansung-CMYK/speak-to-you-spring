package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface ChatRoomErrorCode {
    ErrorMessage ERROR_CHATROOM_NOT_FOUND = new ErrorMessage(404, "chatroom정보가 존재하지 않습니다.");
}
