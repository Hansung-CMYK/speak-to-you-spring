package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface ChatHistoryErrorCode {
    ErrorMessage ERROR_CHATROOM_NOT_EXISTS = new ErrorMessage(404, "chatRoomId를 가지는 채팅방이 없습니다.");
    ErrorMessage ERROR_CHATHISTORY_NOT_EXISTS = new ErrorMessage(404,"해당 ID를 가지는 ChatHistory가 없습니다.");
}
