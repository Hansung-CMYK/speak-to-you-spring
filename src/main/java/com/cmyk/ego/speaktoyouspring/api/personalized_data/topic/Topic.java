package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topic") // 테이블 이름
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="topic_id")
    private Long topicId;

    @Column(name = "diary_id", nullable = false)
    private Long diaryId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "url")
    private String url;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
