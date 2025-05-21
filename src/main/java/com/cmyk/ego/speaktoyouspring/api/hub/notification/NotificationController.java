package com.cmyk.ego.speaktoyouspring.api.hub.notification;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * 사용자 생성
     */
    @Operation(summary = "특정 사용자 단일 알림 생성", description = "사용자 id로 특정 사용자의 알림을 생성한다. egoId는 필수가 아니다.")
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid NotificationDTO notificationDTO) {

        var result = notificationService.create(notificationDTO);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("알림 생성 완료").data(result).build());
    }

    /**
     * 사용자 알림을 수정하는 API (관리자 용)
     */
    @Operation(summary = "특정 사용자 단일 알림 수정", description = "특정 사용자의 알림 데이터를 갱신할 수 있다. notification_id는 필수이다. <br> <히든 값><br> notification_id: int<br>is_read: true/false<br> is_deleted: true/false")
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid NotificationDTO notificationDTO) {
        var result = notificationService.update(notificationDTO);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("알림 수정 완료").data(result).build());
    }

    /**
     * 알림 id로 알림을 삭제하는 API
     */
    @Operation(summary = "특정 사용자 단일 알림 삭제", description = "특정 사용자의 알림을 삭제한다. notification_id는 필수이다.")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity delete(@PathVariable Long notificationId) {
        var result = notificationService.delete(notificationId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("알림 삭제 완료").data(result).build());
    }

    /**
     * 알림 id로 알림을 읽는 API
     */
    @Operation(summary = "특정 사용자 단일 알림 읽기", description = "특정 사용자의 알림을 읽기한다. notification_id는 필수이다. 해당 API를 요청하면 안 읽은 알림 개수가 감소한다.")
    @GetMapping("read/{notificationId}")
    public ResponseEntity read(@PathVariable Long notificationId) {
        var result = notificationService.read(notificationId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("알림 읽기 완료").data(result).build());
    }

    /**
     * 사용자의 안 읽은 알림 개수 조회
     */
    @Operation(summary = "특정 사용자의 안 읽은 알림 개수 조회", description = "특정 사용자의 안 읽은 알림 개수를 조회한다. user_id는 필수이다.")
    @GetMapping("/unread/{userId}")
    public ResponseEntity unreadCount(@PathVariable String userId) {
        var result = notificationService.unreadCount(userId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("알림 개수 조회 완료").data(result).build());
    }

    /**
     * 사용자의 알림 목록 조회
     */
    @Operation(summary = "특정 사용자의 알림 목록 조회", description = "특정 사용자의 알림 목록을 조회한다. user_id는 필수이다.")
    @GetMapping("/{userId}/list")
    public ResponseEntity list(@PathVariable String userId, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10")  int pageSize) {
        var result = notificationService.findNotificationListByUid(userId, pageNum, pageSize);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("알림 목록 조회 완료").data(result).build());
    }
}
