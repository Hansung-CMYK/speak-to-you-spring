package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface NotificationErrorCode {
    ErrorMessage ERROR_NOTIFICATION_NOT_FOUND = new ErrorMessage(404, "알림 정보가 존재하지 않습니다.");
}
