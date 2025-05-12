package com.cmyk.ego.speaktoyouspring.api.personalized_data.egolike;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgoLikeDTO {

    @Schema(hidden = true)
    private Long id;

    @NotNull(message = "uid는 필수값입니다.")
    private String uid;

    @NotNull(message = "egoId는 필수값입니다.")
    private Long egoId;

    private Boolean isLike;

    public EgoLike toEntity() {
        return EgoLike.builder()
                .id(id)
                .uid(uid)
                .egoId(egoId)
                .isLike(isLike)
                .build();
    }

}
