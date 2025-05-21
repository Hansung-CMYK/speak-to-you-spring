package com.cmyk.ego.speaktoyouspring.api.hub.notification;

import com.cmyk.ego.speaktoyouspring.api.hub.ego.Ego;
import com.cmyk.ego.speaktoyouspring.api.hub.ego.EgoApplicationService;
import com.cmyk.ego.speaktoyouspring.api.hub.ego.EgoDTO;
import com.cmyk.ego.speaktoyouspring.api.hub.ego.EgoService;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * 사용자 생성
     * */
    @Operation(summary = "특정 사용자 단일 알림 생성", description = "사용자 id로 ")
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
}
