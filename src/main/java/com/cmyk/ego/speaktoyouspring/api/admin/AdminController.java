package com.cmyk.ego.speaktoyouspring.api.admin;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
    /**
     * 관리자 기능: FCM 직접 발송
     */
    @Operation(summary = "[관리자]: FCM 직접 발송", description = "보낸 즉시 해당 사용자에게 메세지를 보낼 수 있다. (현재 보내는 데이터는 의미 없는 값(발송한 사용자의 id와 보낸 시각)")
    @GetMapping("/fcm/{userId}")
    public ResponseEntity create(@PathVariable String userId) throws FirebaseMessagingException {

        // The topic name can be optionally prefixed with "/topics/".

        // See documentation on defining a message payload.
        Message message = Message.builder().putData("user_id", userId).putData("send_at", String.valueOf(LocalDateTime.now())).setTopic(userId).build();

        // Send a message to the devices subscribed to the provided topic.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);


        return ResponseEntity.ok(CommonResponse.builder().code(200).message("FCM 발송 완료").data(response).build());
    }
}
