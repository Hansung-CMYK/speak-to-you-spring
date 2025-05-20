package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ego_relationship")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgoRelationship {
    /// EGO 고유 ID (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "ego_relationship_id", nullable = false, unique = true)
    private Long egoRelationshipId;

    /// EGO 이름
    @Column(name = "uid", nullable = false)
    private String uid;

    /// EGO 이름
    @Column(name = "ego_id", nullable = false)
    private Long egoId;


    /// EGO 이름
    @Column(name = "relationship_id", nullable = false)
    private Long relationshipId;
}
