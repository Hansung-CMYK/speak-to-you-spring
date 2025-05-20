package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface RelationshipErrorCode {
    ErrorMessage ERROR_RELATIONSHIP_NOT_FOUND = new ErrorMessage(404, "Relationship 정보가 존재하지 않습니다.");
}
