package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgoRelationshipResponseDTO {
    @Schema(hidden = true)
    private Long egoRelationshipId;

    @Schema(example = "user_id_001")
    private String uid;

    @Schema(example = "1")
    private Long egoId;

    @Schema(example = "2")
    private Long relationshipId;

    private LocalDateTime createdAt;

    @Schema(example = "매력적")
    private String relationshipContent;

}
