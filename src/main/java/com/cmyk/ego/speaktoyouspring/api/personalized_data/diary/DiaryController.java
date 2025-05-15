package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.topic.TopicService;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/diary")
@RequiredArgsConstructor
@Validated
public class DiaryController {
    private final DiaryService diaryService;
    private final TopicService topicService;
    private final DiaryApplicationService diaryApplicationService;

    /**
     * 일기 생성하는 API
     */
    @PostMapping
    @Operation(summary = "일기 저장 API", description = "일기의 오늘의 감정과 토픽별 주제/내용/사진을 저장하는 API")
    public ResponseEntity create(@RequestBody @Valid DiaryDTO diaryDTO) {
        var result = diaryApplicationService.create(diaryDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("일기 저장 완료").data(result).build());
    }
}
