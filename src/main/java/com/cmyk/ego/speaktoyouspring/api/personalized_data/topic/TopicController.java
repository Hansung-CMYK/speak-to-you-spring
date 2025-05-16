package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
@Validated
public class TopicController {
    private final TopicService topicService;

    /**
     * 특정 Topic 조회 하는 API
     */
    @GetMapping("{userId}/{topicId}")
    @Operation(summary = "Topic 조회 API", description = "특정 Topic을 조회하는 API")
    public ResponseEntity getTopic(@PathVariable String userId, @PathVariable("topicId") Long topicId) {
        var result = topicService.findByTopicId(userId, topicId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("토픽 삭제 완료").data(result).build());
    }

    /**
     * 특정 Topic 삭제 하는 API
     */
    @DeleteMapping("{userId}/{topicId}")
    @Operation(summary = "Topic 삭제 API", description = "특정 Topic를 삭제하는 API")
    public ResponseEntity ResponseEntity(@PathVariable String userId, @PathVariable("topicId") Long topicId) {
        var result = topicService.deleteById(userId, topicId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("토픽 삭제 완료").data(result).build());
    }

    /**
     * 특정 Topic 사진 수정하는 API
     */
    @PatchMapping(value = "{userId}/{topicId}/url")
    @Operation(summary = "Topic 사진 수정 API", description = "특정 Topic의 사진을 수정하는 API")
    public ResponseEntity updateTopicImage(
            @PathVariable String userId,
            @PathVariable("topicId") Long topicId,
            @RequestBody UrlRequest urlRequest) {
        var result = topicService.updateTopicImage(userId, topicId, urlRequest.getUrl());

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("일기 저장 완료").data(result).build());
    }
}
