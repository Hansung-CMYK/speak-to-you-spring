package com.cmyk.ego.speaktoyouspring.exception;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static com.cmyk.ego.speaktoyouspring.exception.errorcode.BasicErrorCode.DATA_BASE_ERROR;

@Log4j2
@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * ControlledException을 통해 팀원이 직접적으로 추가한 ErrorCode를 제어한다.
     *
     * @param ex 반환된 ErrorCode 정보
     * @return exception.errorcode 패키지에 작성한 정보를 요청 클라이언트로 전송한다.
     */
    @ExceptionHandler(ControlledException.class)
    protected ResponseEntity handleControlledException(ControlledException ex) {
        var code = ex.getErrorCode().getStatus();
        var message = ex.getErrorCode().getMessage();

        log.info("error code: "+code+" message: "+message);
        log.info("explain: "+ ex.getMessage());

        return ResponseEntity.ok(CommonResponse.builder().code(code).message(message).build());
    }

    /**
     * mariadb 내부에서 발생한 오류를 제어한다.
     * ex) id가 조회되지 않는다. 중복된 id를 생성한다. ect...
     *
     * @param ex 반환된 Error 정보 ※ 직접적인 원인은 SpringBoot log를 통해 조회가능
     * @return [404] 데이버베이스 접근 오류
     */
    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity handleDataAccessException(DataAccessException ex) {
        var code = DATA_BASE_ERROR.getStatus();
        var message = DATA_BASE_ERROR.getMessage();

        log.info("error code: "+code+" message: "+message);
        log.info("explain: "+ ex.getMessage());

        return ResponseEntity.ok(CommonResponse.builder().code(code).message(message).build());
    }

    /**
     * [입력값 유효성 검증 실패 시 처리]
     *
     * 이 핸들러는 @Valid 어노테이션이 붙은 객체(UserAccountDTO 등)의 필드 유효성 검사에 실패했을 때,
     * Spring에서 발생시키는 MethodArgumentNotValidException 예외를 처리합니다.
     *
     * 예를 들어, 필수 입력값(uid 등)이 null이거나 형식이 맞지 않으면 이 핸들러가 호출됩니다.
     * 발생한 필드 오류 목록을 순회하며 "필드명: 에러 메시지" 형식으로 가공한 뒤,
     * 클라이언트에 code와 message를 포함한 JSON 응답을 반환합니다.
     *
     * @param ex MethodArgumentNotValidException 예외 객체
     * @return ResponseEntity<CommonResponse> 형식의 400 BAD_REQUEST 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.info("입력값 오류: " + errorMessage);

        return ResponseEntity.badRequest().body(CommonResponse.builder()
                .code(400)
                .message("입력값 오류: " + errorMessage)
                .build());
    }

    /**
     * [ConstraintViolationException 예외 처리]
     *
     * 이 예외는 주로 @Valid 또는 @Validated가 적용된 객체가 아닌,
     * 단일 파라미터 수준에서 유효성 검사를 수행할 때 발생합니다.
     * 예: @RequestParam, @PathVariable 등에 @NotBlank 등 제약조건이 설정된 경우
     *
     * @param ex ConstraintViolationException 예외 객체
     * @return 400 Bad Request 상태 코드와 함께 입력 제약 조건 위반 내용을 포함한 공통 응답
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        log.info("입력값 제약조건 위반: " + errorMessage);

        return ResponseEntity.badRequest().body(CommonResponse.builder()
                .code(400)
                .message("입력값 오류: " + errorMessage)
                .build());
    }
}