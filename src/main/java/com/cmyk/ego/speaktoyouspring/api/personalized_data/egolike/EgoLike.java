package com.cmyk.ego.speaktoyouspring.api.personalized_data.egolike;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ego_like")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgoLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 사용자-에고 관계 ID (자동 증가)

    @Column(name = "uid", nullable = false)
    private String uid; // 사용자 UID

    @Column(name = "ego_id", nullable = false)
    private Long egoId;

    @Column(name = "is_like")
    private Boolean isLike = false;  // 좋아요 여부
}
