package com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationReadRequest {
    @NotBlank(message = "uid는 필수값입니다.")
    private String uid;
}
