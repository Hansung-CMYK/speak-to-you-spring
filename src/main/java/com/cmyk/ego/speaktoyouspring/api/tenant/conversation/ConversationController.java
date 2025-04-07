package com.cmyk.ego.speaktoyouspring.api.tenant.conversation;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversation/")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("create/{schemaName}")
    public ResponseEntity create(@PathVariable String schemaName, @RequestBody Conversation conversation) {
        TenantContext.setCurrentTenant(schemaName);
        var result = conversationService.create(conversation);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 생성 완료").data(result).build());
    }

    // Schema안에서 Topic의 대화내용을 전달
    @GetMapping("{schemaName}/{logId}/conversations")
    public ResponseEntity getConversation(@PathVariable String schemaName, @PathVariable Long logId) {
        try {
            TenantContext.setCurrentTenant(schemaName);

            var conversations = conversationService.getConversations(logId);

            return ResponseEntity.ok(CommonResponse.builder()
                    .code(200)
                    .message("대화 내역 조회")
                    .data(conversations)
                    .build());

        } catch (EntityNotFoundException e) { // 대화내용이 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CommonResponse.builder()
                            .code(404)
                            .message(e.getMessage())
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.builder()
                            .code(500)
                            .message("요청 처리 중 오류 발생: " + e.getMessage())
                            .build());
        } finally {
            TenantContext.clear();
        }
    }
}