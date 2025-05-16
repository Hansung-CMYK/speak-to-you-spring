package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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

    @Schema(
            description = "Base64 인코딩된 이미지 데이터",
            type = "string",
            format = "byte",
            example = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACN"
    )
    private String picture;

    @Schema(example = "false")
    private Boolean isDeleted;

    public Topic toEntity() {

        return Topic.builder()
                .topicId(topicId)
                .diaryId(diaryId)
                .title(title)
                .content(content)
                .picture(decodedPicture(picture))
                .isDeleted(isDeleted)
                .build();
    }

    public static byte[] decodedPicture(String encodedPicture) {
        byte[] decodedPicture = null;
        if (encodedPicture != null && !encodedPicture.isBlank()) {
            try {
                decodedPicture = Base64.getDecoder().decode(encodedPicture);
            } catch (IllegalArgumentException e) {
                // 디코딩 실패 시 로그 출력
                System.err.println("Base64 decoding failed: " + e.getMessage());
            }
        }
        System.out.println("Decoded picture size: " + (decodedPicture != null ? decodedPicture.length : "null"));

        return decodedPicture;
    }

    public static String encodedPicture(byte[] picture) {
        String encodedPicture = null;
        if (picture != null && picture.length > 0) {
            encodedPicture = Base64.getEncoder().encodeToString(picture);
        }
        return encodedPicture;
    }
}
