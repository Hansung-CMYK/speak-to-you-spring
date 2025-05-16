package com.cmyk.ego.speaktoyouspring.api.hub.personality;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentListRequest {
    @Schema(example = "[\"늘정주나\", \"시간마술사\", \"존댓말자동완성\", \"식사개근\", \"유교휴먼\", \"TMI장인\", \"콘센트헌터\"]")
    private List<String> contentList;
}
