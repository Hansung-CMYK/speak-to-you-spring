package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "diary")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="diary_id")
    private Long diaryId;

    @Column(name = "uid", nullable = false)
    private String uid; // 사용자 고유 ID

    @Column(name = "ego_id", nullable = false)
    private Long egoId; // EGO 고유 ID

    @Column(name = "feeling")
    private String feeling; // 오늘의 감정

    @Column(name = "created_at")
    private LocalDate createdAt;
}
