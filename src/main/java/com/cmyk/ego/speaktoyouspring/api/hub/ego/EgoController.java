package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ego/")
@RequiredArgsConstructor
@Validated
public class EgoController {
    private final EgoService egoService;

    @PostMapping("create")
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

        var result = egoService.create(egoDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 생성 완료").data(result).build());
    }

    @GetMapping("read/all")
    public ResponseEntity readAll() {

        var result = egoService.readAll();

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 조회 완료").data(result).build());
    }

}
