package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgoRelationshipDTO {
    @Schema(hidden = true)
    private Long egoRelationshipId;

    @Schema(example = "user_id_001")
    private String uid;

    @Schema(example = "1")
    private Long egoId;

    @Schema(example = "2")
    private Long relationshipId;

    @Schema(example = "2025-05-19T23:30:25.998", hidden = true)
    private LocalDateTime createdAt;

    public EgoRelationship toEntity() {
        return EgoRelationship.builder()
                .egoRelationshipId(egoRelationshipId)
                .uid(uid)
                .egoId(egoId)
                .relationshipId(relationshipId)
                .createdAt(createdAt)
                .build();
    }
}
