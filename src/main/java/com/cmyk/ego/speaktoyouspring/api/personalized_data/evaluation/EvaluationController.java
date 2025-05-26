package com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/evaluation")
@RequiredArgsConstructor
@Validated
public class EvaluationController {
    private final EvaluationService evaluationService;
    private final UserAccountRepository userAccountRepository;

    /**
     * 평가 결과 생성
     * */
    @Operation(summary = "사용자가 작성한 평가 결과 저장/수정", description = "사용자가 작성한 평가 결과를 저장합니다.<br>만약, 평가 결과가 존재한다면, 에고id와 uid로 검색해 점수를 수정합니다.", tags = {"평가"})
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid EvaluationDTO evaluationDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        userAccountRepository.findByUid(evaluationDTO.getUid())
                .orElseThrow(() -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(evaluationDTO.getUid());

        var result = evaluationService.create(evaluationDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("evaluation 생성 완료").data(result).build());
    }

    /**
     * 평가 결과 전체 조회
     * 필수값 : uid
     * */
    @Operation(summary = "uid로 작성한 평가 결과 전체 조회 API", description = "uid를 입력받아 해당 uid의 모든 평가 결과를 조회합니다.", tags = {"평가"})
    @PostMapping("/list")
    public ResponseEntity readAll(@RequestBody @Valid EvaluationReadRequest evaluationReadRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        userAccountRepository.findByUid(evaluationReadRequest.getUid())
                .orElseThrow(() -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(evaluationReadRequest.getUid());

        var result = evaluationService.readAll();

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("evaluation 전체 조회 완료").data(result).build());
    }

}
