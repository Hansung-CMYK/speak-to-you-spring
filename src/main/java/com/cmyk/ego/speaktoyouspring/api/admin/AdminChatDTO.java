package com.cmyk.ego.speaktoyouspring.api.admin;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminChatDTO {
    @NotNull(message = "uid는 필수값입니다.")
    private String uid;

    @NotNull(message = "egoId는 필수값입니다.")
    private Long egoId;

    private List<AdminChatHistoryDTO> content;
}