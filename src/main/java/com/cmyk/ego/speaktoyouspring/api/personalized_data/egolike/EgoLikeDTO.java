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

    @Schema(example = "user_id_001")
    @NotNull(message = "uid는 필수값입니다.")
    private String uid;

    @Schema(example = "1")
    @NotNull(message = "egoId는 필수값입니다.")
    private Long egoId;

    @Schema(example = "false")
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
