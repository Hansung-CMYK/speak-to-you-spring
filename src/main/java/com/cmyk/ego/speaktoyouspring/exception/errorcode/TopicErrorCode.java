package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface TopicErrorCode {
    ErrorMessage ERROR_TOPIC_NOT_FOUND = new ErrorMessage(404, "Topic 정보가 존재하지 않습니다.");
}
