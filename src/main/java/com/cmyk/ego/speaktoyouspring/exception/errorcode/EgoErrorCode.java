package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface EgoErrorCode {
    ErrorMessage ERROR_EGO_NOT_FOUND = new ErrorMessage(404, "ego정보가 존재하지 않습니다.");
}
