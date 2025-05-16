package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface PersonalityErrorCode {
    ErrorMessage ERROR_PERSONALITY_ID_NOT_FOUND = new ErrorMessage(404, "성격 ID 정보가 존재하지 않습니다.");
    ErrorMessage ERROR_PERSONALITY_CONTENT_NOT_FOUND = new ErrorMessage(404, "성격 내용 정보가 존재하지 않습니다.");
}
