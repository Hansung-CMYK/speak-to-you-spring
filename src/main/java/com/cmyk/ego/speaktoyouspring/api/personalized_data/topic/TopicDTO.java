package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

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

    @Schema(example = "https://fal.media/files/penguin/vX86khImmFa6A8JgBpD0g_66de42358e1146a8a4b689d1e0e96e1d.jpg")
    private String url;

    @Schema(example = "false")
    private Boolean isDeleted;

    public Topic toEntity() {

        return Topic.builder()
                .topicId(topicId)
                .diaryId(diaryId)
                .title(title)
                .content(content)
                .url(url)
                .isDeleted(isDeleted)
                .build();
    }

    public static byte[] decodedUrl(String encodedUrl) {
        byte[] decodedUrl = null;
        if (encodedUrl != null && !encodedUrl.isBlank()) {
            try {
                decodedUrl = Base64.getDecoder().decode(encodedUrl);
            } catch (IllegalArgumentException e) {
                // 디코딩 실패 시 로그 출력
                System.err.println("Base64 decoding failed: " + e.getMessage());
            }
        }

        return decodedUrl;
    }

    public static String encodedUrl(byte[] url) {
        String encodedUrl = null;
        if (url != null && url.length > 0) {
            encodedUrl = Base64.getEncoder().encodeToString(url);
        }
        return encodedUrl;
    }
}
