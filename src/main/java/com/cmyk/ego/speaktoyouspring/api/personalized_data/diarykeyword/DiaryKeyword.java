package com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diary_keyword")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class DiaryKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="keyword_id")
    private Long keywordId;

    @Column(name = "diary_id", nullable = false)
    private Long diaryId;

    @Column(name = "content")
    private String content;
}
