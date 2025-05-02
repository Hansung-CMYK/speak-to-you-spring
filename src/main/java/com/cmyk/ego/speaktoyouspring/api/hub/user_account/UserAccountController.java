package com.cmyk.ego.speaktoyouspring.api.hub.user_account;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.flyway.FlywayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-account/")
@RequiredArgsConstructor
@Validated
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final FlywayService flywayService;

    @PostMapping("create")
    public ResponseEntity create(@RequestBody @Valid UserAccountDTO userAccountDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        var result = flywayService.createUserSchema(userAccountDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("스키마 생성 완료").data(result).build());
    }

    // user_account table의 전체 정보를 조회한다.
    @GetMapping("read/all")
    public ResponseEntity readAll() {
        var result = userAccountService.getAllUserData();

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("목록 조회 완료").data(result).build());
    }

    // user_account table에서 특정 email을 가지는 user를 조회한다.
    @GetMapping("read")
    public ResponseEntity readByEmail(@RequestParam String email) {

        var result = userAccountService.getUserDataByEmail(email);

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message("사용자 조회 성공")
                .data(result)
                .build());
    }

    @PatchMapping("update")
    public ResponseEntity updateUserAccount(
            @RequestBody @Valid UserAccountDTO updateUserAccountReq, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        var result = userAccountService.updateUserAccount(updateUserAccountReq);

        return ResponseEntity.ok(CommonResponse.builder()
                .code(200)
                .message("사용자 정보 업데이트 성공")
                .data(result)
                .build());
    }

    // user_account table에서 특정 uid를 가지는 user를 탈퇴 시킨다.
    @DeleteMapping("delete")
    public ResponseEntity delete(@RequestBody UserDeleteRequest targetUser) {
        var result = flywayService.deleteTenant(targetUser);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("사용자 탈퇴 완료").data(result).build());
    }
}
