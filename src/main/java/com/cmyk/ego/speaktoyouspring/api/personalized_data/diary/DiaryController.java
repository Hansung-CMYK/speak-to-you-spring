package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.topic.TopicService;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 일기 조회하는 API
     */
    @PostMapping("/{userId}/{dirayId}")
    @Operation(summary = "일기 조회 API", description = "특정 일기 ID로 일기 내용 전체(토픽 호함)를 조회하는 API")
    public ResponseEntity readAll(@PathVariable String userId, @PathVariable Long dirayId) {
        var result = diaryApplicationService.findByDiaryId(userId, dirayId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("일기 조회 완료").data(result).build());
    }

    /**
     * 오늘의 한 줄 요약 추가/수정하는 API
     */
    /**
    @PostMapping("/{userId}/{dirayId}/comment")
    @Operation(summary = "오늘의 한 줄 요약 수정 API", description = "저장된 일기의 오늘의 한 줄 요약을 추가/수정하는 API")
    public ResponseEntity updateComment(@PathVariable String userId, @PathVariable Long dirayId, @RequestBody @Valid String dailyComment) {
        var result = diaryService.updateComment(userId, dirayId, dailyComment);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("오늘의 한 줄 요약 수정 완료").data(result).build());
    }
    */
}
