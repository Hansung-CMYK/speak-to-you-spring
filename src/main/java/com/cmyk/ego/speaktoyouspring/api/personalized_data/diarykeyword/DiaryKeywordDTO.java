package com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryKeywordDTO {
    @Schema(hidden = true)
    private Long keywordId;

    @NotNull(message = "diaryId는 필수입니다.")
    @Schema(example = "1")
    private Long diaryId;

    @Schema(example = "아니 근데")
    private String content;

    public DiaryKeyword toEntity() {
        return DiaryKeyword.builder()
                .keywordId(keywordId)
                .diaryId(diaryId)
                .content(content)
                .build();
    }
}
