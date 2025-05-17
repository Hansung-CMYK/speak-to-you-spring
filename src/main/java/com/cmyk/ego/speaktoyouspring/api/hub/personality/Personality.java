package com.cmyk.ego.speaktoyouspring.api.hub.personality;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personality") // 테이블 이름
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Personality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "personality_id", nullable = false, unique = true)
    private Long personalityId;

    @Column(name = "content", nullable = false, unique = true)
    private String content;

    @Column(name = "image_url")
    private String imageUrl;
}
