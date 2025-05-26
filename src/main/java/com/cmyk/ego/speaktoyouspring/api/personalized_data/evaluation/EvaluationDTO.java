package com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationDTO {

    @Schema(hidden = true)
    private Long id; // 평가 고유 ID (자동 증가)

    @Schema(example = "user_id_001")
    @NotBlank(message = "UID는 필수입니다.")
    private String uid; // 사용자 고유 ID

    @Schema(example = "1")
    @NotNull(message = "egoId는 필수입니다.")
    private Long egoId; // EGO 고유 ID

    @Schema(example = "1")
    @NotNull(message = "solvingScore는 필수입니다.")
    private Integer solvingScore; // 문제 해결 능력 (1~3 점)

    @Schema(example = "2")
    @NotNull(message = "talkingScore는 필수입니다.")
    private Integer talkingScore; // 공감 능력 (1~3 점)

    @Schema(example = "3")
    @NotNull(message = "overallScore는 필수입니다.")
    private Integer overallScore; // 총평 (1~5 점)


    public Evaluation toEntity() {
        return Evaluation.builder()
                .uid(uid)
                .egoId(egoId)
                .solvingScore(solvingScore)
                .talkingScore(talkingScore)
                .overallScore(overallScore)
                .build();
    }
}