package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgoDTO {

    @Schema(hidden = true)
    private Long id;                      // EGO 고유 ID

    @Schema(example = "카리나")
    @NotBlank(message = "name은 필수입니다.")
    private String name;                     // EGO 이름

    @NotBlank(message = "introduction은 필수입니다.")
    private String introduction;             // EGO 자기소개

    @Schema(example = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACN")
    private byte[] profileImage;             // EGO 프로필 이미지

    @Schema(example = "INTJ")
    @NotBlank(message = "mbti는 필수입니다.")
    private String mbti;                     // MBTI 성격 유형

    @Schema(example = "2025-05-06")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;             // 생성 날짜

    @Schema(example = "1000")
    private Long likes;

    @Schema(example = "[\"늘정주나\", \"시간마술사\", \"존댓말자동완성\", \"식사개근\", \"유교휴먼\", \"TMI장인\", \"콘센트헌터\"]")
    private List<String> personalityList;

    public Ego toEntity() {
        return Ego.builder()
                .id(id)
                .name(name)
                .introduction(introduction)
                .profileImage(profileImage)
                .mbti(mbti)
                .createdAt(createdAt)
                .likes(likes)
                .build();
    }
}
