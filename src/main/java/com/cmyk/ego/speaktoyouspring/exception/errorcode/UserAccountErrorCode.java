package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface UserAccountErrorCode {
    ErrorMessage ERROR_USER_NOT_FOUND = new ErrorMessage(404, "user정보가 존재하지 않습니다.");
}
