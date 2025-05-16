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
    @Schema(example = "[{\"content\":\"늘정주나\", \"imageUrl\": \"assets/image/game_chat_room.png\"}, {\"content\":\"시간마술사\", \"imageUrl\": \"assets/image/movie_chat_room.png\"}, {\"content\":\"존댓말자동완성\", \"imageUrl\": \"assets/image/music_chat_room.png\"}, {\"content\":\"유교휴먼\", \"imageUrl\": \"assets/image/anime_chat_room.png\"}]")
    private List<ContentRequest> contentList;
}