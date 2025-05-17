package com.cmyk.ego.speaktoyouspring.api.personalized_data.calendar;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
@Validated
public class CalendarController {
    private final CalendarApplicationSerivce calendarApplicationSerivce;
    /**
     * user_id로 월별 일기 리스트 조회하는 API
     */
    @Operation(summary = "월별 캘린더 리스트 조회 API", description = "user_id로 월별 일기 리스트를 조회하는 API, 년(YYYY), 월(M)을 쿼리스트링으로 추가할 수 있고 해당 년월에 앞뒤 1개월까지 리스트로 반환한다.<br><br>" +
            "각 path의 값의 도메인은 다음과 같다."+
            "<li>행복: assets/icon/emotion/happiness.svg</li><br>" +
            "<li>평범: assets/icon/emotion/embarrassment.svg</li><br>" +
            "<li>불안: assets/icon/emotion/disappointment.svg</li><br>" +
            "<li>화남: assets/icon/emotion/anger.svg</li><br>" +
            "<li>슬픔: assets/icon/emotion/sadness.svg</li>")
    @GetMapping("/{userId}")
    public ResponseEntity findByMonth(@PathVariable String userId, @RequestParam(defaultValue = "5") int month, @RequestParam(defaultValue = "2025") int year) {

        var result = calendarApplicationSerivce.findByMonth(userId, month, year);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("월별 캘린더 조회 완료").data(result).build());
    }
}
