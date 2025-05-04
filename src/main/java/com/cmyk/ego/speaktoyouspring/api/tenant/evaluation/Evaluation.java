package com.cmyk.ego.speaktoyouspring.api.tenant.evaluation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluation") // 테이블 이름
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "uid", nullable = false)
    private String uid; // 사용자 고유 ID

    @Column(name = "ego_id", nullable = false)
    private Long egoId; // EGO 고유 ID

    @Column(name = "solving_score")
    private Integer solvingScore; // 문제 해결 능력 (1~3 점)

    @Column(name = "talking_score")
    private Integer talkingScore; // 공감 능력 (1~3 점)

    @Column(name = "overall_score")
    private Integer overallScore; // 총평 (1~5 점)
}