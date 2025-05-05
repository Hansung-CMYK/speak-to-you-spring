package com.cmyk.ego.speaktoyouspring.api.hub.user_account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestBody {
    private String uid;
    private String email;
}
