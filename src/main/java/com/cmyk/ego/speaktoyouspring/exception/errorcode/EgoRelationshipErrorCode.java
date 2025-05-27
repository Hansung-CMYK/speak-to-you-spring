package com.cmyk.ego.speaktoyouspring.exception.errorcode;

import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;

public interface EgoRelationshipErrorCode {
    ErrorMessage ERROR_EGO_RELATIONSHIP_NOT_FOUND = new ErrorMessage(404, "Ego Relationship 정보가 존재하지 않습니다.");
}
