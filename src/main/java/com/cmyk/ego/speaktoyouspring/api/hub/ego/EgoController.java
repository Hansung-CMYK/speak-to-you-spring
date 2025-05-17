package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ego")
@RequiredArgsConstructor
@Validated
public class EgoController {
    private final EgoService egoService;
    private final EgoApplicationService egoApplicationService;

    /**
     * ego 생성
     * */
    @Operation(summary = "ego 생성", description = "ego정보를 생성한다.")
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid EgoDTO egoDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        Ego result = egoService.create(egoDTO);
        egoApplicationService.savePersonality(result.getId(), egoDTO.getPersonalityList());

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 생성 완료").data(result).build());
    }

    /**
     * ego테이블에 기록된 전체 ego조회
     */
    @Operation(summary = "전체 ego 조회", description = "전체 ego정보를 조회한다.")
    @GetMapping
    public ResponseEntity readAll() {

        var result = egoService.readAll();

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 조회 완료").data(result).build());
    }

    /**
     * egoid와 일치하는 ego조회
     * */
    @Operation(summary = "ego 정보 조회", description = "egoId를 기준으로 ego정보를 조회한다.")
    @GetMapping("/{egoid}")
    public ResponseEntity read(@PathVariable("egoid") Long egoId) {

        var result = egoApplicationService.getEgoInfo(egoId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 조회 완료").data(result).build());
    }

    /**
     * ego정보 수정
     * */
    @Operation(summary = "ego 정보 수정", description = "egoId를 기준으로 ego정보를 수정한다.")
    @PatchMapping
    public ResponseEntity update(@RequestBody EgoDTO egoDTO) {

        if (egoDTO.getId() == null) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("id는 EGO 테이블의 id 값으로 업데이트시 필수 값입니다. (예: \"id\": 8)")
                    .build());
        }

        var result = egoApplicationService.updateEgoInfo(egoDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 수정 완료").data(result).build());
    }

    /**
     * EGO 정보 불러오기
     */
    @Operation(summary = "ego 정보 불러오기", description = "userId를 기준으로 ego정보를 불러온다.")
    @GetMapping("/{userid}/list")
    public ResponseEntity getUserEgoList(@PathVariable("userid") String userId) {

        List<Ego> result = egoApplicationService.getUserEgoList(userId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("연관된 ego 목록 조회 완료").data(result).build());
    }
}
