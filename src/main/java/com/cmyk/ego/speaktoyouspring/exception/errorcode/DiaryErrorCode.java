package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface DiaryErrorCode {
    ErrorMessage ERROR_DIARY_NOT_FOUND = new ErrorMessage(404, "diary 정보가 존재하지 않습니다.");
}
