package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

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

    public EgoRelationship toEntity() {
        return EgoRelationship.builder()
                .egoRelationshipId(egoRelationshipId)
                .uid(uid)
                .egoId(egoId)
                .relationshipId(relationshipId)
                .build();
    }
}
