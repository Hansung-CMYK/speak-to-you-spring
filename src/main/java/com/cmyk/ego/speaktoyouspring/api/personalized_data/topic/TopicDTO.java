package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {

    @Schema(hidden = true)
    private Long topicId;

    @NotNull(message = "diaryId는 필수입니다.")
    @Schema(example = "1")
    private Long diaryId;

    @Schema(example = "웃음이 가득한 아침 식사")
    private String title;

    @Schema(example = "오늘은 가족들과 웃음이 가득한 아침 식사를 했어요. 맛있는 퐁듀를 2그릇이나 먹었어요.")
    private String content;

    @Schema(example = "false")
    private Boolean isDeleted;

    public Topic toEntity() {
        return Topic.builder()
                .topicId(topicId)
                .diaryId(diaryId)
                .title(title)
                .content(content)
                .isDeleted(isDeleted)
                .build();
    }
}
