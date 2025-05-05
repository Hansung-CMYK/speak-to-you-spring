package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface EvaluationErrorCode {
    ErrorMessage ERROR_SCORE_OUT_OF_RANGE = new ErrorMessage(400, "평가 점수가 범위를 벗어났습니다.");
}
