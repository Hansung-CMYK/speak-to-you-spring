package com.cmyk.ego.speaktoyouspring.api.hub.ego_personality;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ego_personality") // 테이블 이름
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EgoPersonality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "ego_personality_id", nullable = false, unique = true)
    private Long egoPersonalityId;

    @Column(name = "ego_id", nullable = false)
    private Long egoId;

    @Column(name = "personality_id", nullable = false)
    private Long personalityId;
}
