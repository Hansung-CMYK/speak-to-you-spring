package com.cmyk.ego.speaktoyouspring.api.personalized_data.conversation;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import lombok.RequiredArgsConstructor;
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
}
