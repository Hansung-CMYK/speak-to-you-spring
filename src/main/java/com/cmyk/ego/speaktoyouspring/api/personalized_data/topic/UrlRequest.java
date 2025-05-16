package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequest {
    @Schema(example = "https://fal.media/files/penguin/vX86khImmFa6A8JgBpD0g_66de42358e1146a8a4b689d1e0e96e1d.jpg")
    private String url;
}
