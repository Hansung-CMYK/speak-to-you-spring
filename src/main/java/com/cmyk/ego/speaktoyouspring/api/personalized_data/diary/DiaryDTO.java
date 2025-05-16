package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.topic.Topic;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.topic.TopicDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryDTO {
    @Schema(hidden = true)
    private Long diaryId;

    @NotNull(message = "UID는 필수입니다.")
    @Schema(example = "user_id_001")
    private String uid; // 사용자 고유 ID

    @NotNull(message = "egoId는 필수입니다.")
    @Schema(example = "1")
    private Long egoId; // EGO 고유 ID

    @Schema(example = "활기찬, 뿌듯한")
    private String feeling; // 오늘의 감정

    @Schema(example = "오늘은 아주 활발한 하루였어요! 특히 냥 체를 많이 사용하셨네요!")
    private String dailyComment;

    @NotNull(message = "일기 생성 날짜는 필수 입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2025-05-06")
    private LocalDate createdAt; // 일기 생성 날짜

    @Schema(example = "[\"엥\", \"진짜?\", \"아니\", \"근데\"]")
    private List<String> keywords; // 일기 일별 키워드
   
    
   @Schema(example = "[{\"title\": \"웃음이 가득한 아침 식사\", \"content\": \"오늘은 가족들과 웃음이 가득한 아침 식사를 했어요. 맛있는 퐁듀를 2그릇이나 먹었어요.\", \"url\": \"https://fal.media/files/penguin/vX86khImmFa6A8JgBpD0g_66de42358e1146a8a4b689d1e0e96e1d.jpg\", \"isDeleted\": false}, {\"title\": \"나른한 오후의 만난 삼색 고양이\", \"content\": \"노을이 질 때 즈음에 삼색 고양이를 봤어요. 참치를 간식으로 주었어요.\", \"url\": \"https://fal.media/files/penguin/vX86khImmFa6A8JgBpD0g_66de42358e1146a8a4b689d1e0e96e1d.jpg\", \"isDeleted\": false}]")
   private List<TopicDTO> topics;
   
    public Diary toEntity() {
        return Diary.builder()
                .diaryId(diaryId)
                .uid(uid)
                .egoId(egoId)
                .createdAt(createdAt)
                .dailyComment(dailyComment)
                .build();
    }
}
