package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ego") // 테이블 이름
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Ego {

    /// EGO 고유 ID (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /// EGO 이름
    @Column(name = "name", nullable = false)
    private String name;

    /// EGO 자기소개
    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    /// EGO 프로필 이미지 (Byte 데이터)
    @Column(name = "profile_image", columnDefinition = "BYTEA")
    private byte[] profileImage;

    /// EGO MBTI 성격 유형
    @Column(name = "mbti", length = 4)
    private String mbti;

    /// EGO 생성 날짜
    @Builder.Default
    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    /// EGO 좋아요 개수
    @Column(name = "likes")
    private Long likes;

    private Integer rating;
}