package com.cmyk.ego.speaktoyouspring.api.hub.relationship;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "relationship")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
    /// Relationship 고유 ID (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "relationship_id", nullable = false, unique = true)
    private Long relationshipId;

    /// 관계 내용
    @Column(name = "relationship_content", nullable = false)
    private String relationshipContent;
}
