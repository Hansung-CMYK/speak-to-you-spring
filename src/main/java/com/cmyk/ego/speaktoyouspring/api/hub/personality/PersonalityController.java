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
    @Operation(summary = "성격 전체 조회 API", description = "테이블 내 모든 성격을 확인할 수 있습니다.")
    @GetMapping("/all")
    public ResponseEntity findAll() {
        var result = personalityService.findAll();
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("personality 전체 조회 완료").data(result).build());
    }


    /**
     * 성격 추가
     */
    @Operation(summary = "성격 추가 API", description = "해당하는 성격이 기존 테이블에 있다면, 중복되게 추가되지 않고, \n url을 변경하면 해당 성격의 URL이 업데이트 됩니다.")
    @PostMapping("")
    public ResponseEntity add(@RequestBody ContentRequest contentRequest) {
        var result = personalityService.add(contentRequest.getContent());
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("성격 추가 완료").data(result).build());
    }

    /**
     * 성격 리스트 추가
     */
    @Operation(summary = "성격 리스트 추가 API", description = "해당하는 성격이 기존 테이블에 있다면, 중복되게 추가되지 않고, \n url을 변경하면 해당 성격의 URL이 업데이트 됩니다.")
    @PostMapping("/list")
    public ResponseEntity addList(@RequestBody ContentListRequest contentListRequest) {
        var result = personalityService.addList(contentListRequest);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("성격 리스트 추가 완료").data(result).build());
    }

    /**
     * 성격 전체 삭제
     */
    @Operation(summary = "성격 전체 삭제 API *테스트용으로도 실행을 권장하지 않음*", description = "성격 테이블에 모든 정보가 완전 삭제되기 때문에 에고 성격과 연동해놓았다면, \n 에고 정보를 조회할때 오류가 날 수 있습니다. \n 이 경우, 에고 테이블에서 수정 API를 추가로 호출하셔야 합니다.")
    @DeleteMapping("")
    public ResponseEntity deleteAll() {
        personalityService.deleteAll();
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("성격 전체 삭제 완료").build());
    }
}
