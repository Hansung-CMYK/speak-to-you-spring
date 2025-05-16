package com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diary-keyword")
@RequiredArgsConstructor
@Validated
public class DiaryKeywordController {
    private final DiaryKeywordService diaryKeywordService;

    /**
     * diaryId에 해당하는 키워드를 모두 조회한다.
     */
    @GetMapping("{userId}/{diaryId}")
    @Operation(summary = "일기 키워드 리스트 조회 API")
    public ResponseEntity findByDiaryId(@PathVariable String userId, @PathVariable("diaryId") Long diaryId) {
        var result = diaryKeywordService.findByDiaryId(userId, diaryId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("일기 키워드 리스트 조회 완료").data(result).build());
    }

    /**
     * 키워드 리스트를 추가한다.
     */
    @PostMapping("{userId}/list")
    @Operation(summary = "일기 키워드 리스트 저장 API")
    public ResponseEntity create(@PathVariable String userId, @RequestBody List<DiaryKeywordDTO> diaryKeywordDTOList) {
        diaryKeywordService.saveAll(userId, diaryKeywordDTOList);

        var result = diaryKeywordService.findByDiaryId(userId, diaryKeywordDTOList.getFirst().getDiaryId());

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("일기 키워드 리스트 저장 완료").data(result).build());
    }


}
