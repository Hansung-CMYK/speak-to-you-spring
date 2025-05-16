package com.cmyk.ego.speaktoyouspring.api.hub.personality;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/personality")
@RequiredArgsConstructor
@Validated
public class PersonalityController {
    private final PersonalityService personalityService;

    /**
     * personality_id로 특정 성격 조회
     */
    @Operation(summary = "특정 성격 조회 API")
    @GetMapping("/{personalityId}")
    public ResponseEntity findById(@PathVariable Long personalityId) {
        var result = personalityService.findByPersonalityId(personalityId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("특정 personality 조회 완료").data(result).build());
    }

    /**
     * 성격 전체 조회
     */
    @Operation(summary = "성격 전체 조회 API")
    @GetMapping("/all")
    public ResponseEntity findAll() {
        var result = personalityService.findAll();
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("personality 전체 조회 완료").data(result).build());
    }


    /**
     * 성격 추가
     */
    @Operation(summary = "성격 추가 API")
    @PostMapping("/")
    public ResponseEntity add(@RequestBody ContentRequest contentRequest) {
        var result = personalityService.add(contentRequest.getContent());
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("성격 추가 완료").data(result).build());
    }

    /**
     * 성격 리스트 추가
     */
    @Operation(summary = "성격 리스트 추가 API")
    @PostMapping("/list")
    public ResponseEntity addList(@RequestBody ContentListRequest contentListRequest) {
        var result = personalityService.addList(contentListRequest);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("성격 리스트 추가 완료").data(result).build());
    }

    /**
     * 성격 전체 삭제
     */
    @Operation(summary = "성격 전체 삭제 API")
    @DeleteMapping("/")
    public ResponseEntity deleteAll() {
        personalityService.deleteAll();
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("성격 전체 삭제 완료").build());
    }
}
