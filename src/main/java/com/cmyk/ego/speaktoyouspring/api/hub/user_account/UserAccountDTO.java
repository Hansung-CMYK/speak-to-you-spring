package com.cmyk.ego.speaktoyouspring.api.hub.user_account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserAccountDTO {

    @NotBlank(message = "UID는 필수입니다.")
    private String uid;

    @NotBlank(message = "email은 필수입니다.")
    private String email;

    @NotNull(message = "생일(birthDate)은 필수입니다.")
    private LocalDate birthDate;

    @NotBlank(message = "role은 필수입니다.")
    private String role;

    private LocalDate createdAt;

    private Boolean isDeleted;

    public UserAccount toEntity() {
        return UserAccount.builder()
                .uid(uid)
                .email(email)
                .birthDate(birthDate)
                .role(role)
                .createdAt(createdAt)
                .isDeleted(isDeleted)
                .build();
    }
}
