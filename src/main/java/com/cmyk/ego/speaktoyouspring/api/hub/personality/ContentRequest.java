package com.cmyk.ego.speaktoyouspring.api.hub.personality;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequest {
    @Schema(example = "늘정주나")
    private String content;
}
