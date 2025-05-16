package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgoDTO {

    private Long id;                      // EGO 고유 ID

    @NotBlank(message = "name은 필수입니다.")
    private String name;                     // EGO 이름

    @NotBlank(message = "introduction은 필수입니다.")
    private String introduction;             // EGO 자기소개

    private byte[] profileImage;             // EGO 프로필 이미지

    @NotBlank(message = "mbti는 필수입니다.")
    private String mbti;                     // MBTI 성격 유형

    @NotBlank(message = "personality는 필수입니다.")
    private String personality;              // EGO 성격 설명

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;             // 생성 날짜

    private Long likes;

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
