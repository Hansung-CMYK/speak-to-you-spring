package com.cmyk.ego.speaktoyouspring.api.personalized_data.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDTO {

    private Long id;                      // diray_id

    private LocalDate createdAt;             // EGO 프로필 이미지

    private String path;                     // MBTI 성격 유형
}
