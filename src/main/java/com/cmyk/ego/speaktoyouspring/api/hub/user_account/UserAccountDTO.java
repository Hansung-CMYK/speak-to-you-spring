package com.cmyk.ego.speaktoyouspring.api.hub.user_account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserAccountDTO {

    @Schema(example = "user_id_001")
    @NotBlank(message = "UID는 필수입니다.")
    private String uid;

    @Schema(example = "1")
    private Long egoId;

    @Schema(example = "cmyk1219@gmail.com")
    @NotBlank(message = "email은 필수입니다.")
    private String email;

    @NotNull(message = "생일(birthDate)은 필수입니다.")
    private LocalDate birthDate;

    @Schema(example = "ROLE_USER")
    @NotBlank(message = "role은 필수입니다.")
    private String role;

    @Schema(example = "2025-05-06", hidden = true)
    private LocalDate createdAt;

    @Schema(example = "false", hidden = true)
    private Boolean isDeleted;

    public UserAccount toEntity() {
        return UserAccount.builder()
                .uid(uid)
                .egoId(egoId)
                .email(email)
                .birthDate(birthDate)
                .role(role)
                .createdAt(createdAt)
                .isDeleted(isDeleted)
                .build();
    }
}
