package com.cmyk.ego.speaktoyouspring.api.hub.relationship;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipDTO {
    @Schema(hidden = true)
    private Long relationshipId;

    @Schema(example = "매력적")
    private String relationshipContent;

    public Relationship toEntity() {
        return Relationship.builder()
                .relationshipId(relationshipId)
                .relationshipContent(relationshipContent)
                .build();
    }
}
