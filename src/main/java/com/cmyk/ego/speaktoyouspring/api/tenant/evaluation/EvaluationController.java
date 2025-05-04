package com.cmyk.ego.speaktoyouspring.api.tenant.evaluation;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/evaluation/")
@RequiredArgsConstructor
@Validated
public class EvaluationController {
    private final EvaluationService evaluationService;
    private final UserAccountRepository userAccountRepository;

    @PostMapping("create")
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

    @PostMapping("read/all")
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
