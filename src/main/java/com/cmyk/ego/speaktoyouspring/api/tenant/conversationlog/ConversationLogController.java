package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversationLog/")
@RequiredArgsConstructor
public class ConversationLogController {
    private final ConversationLogService conversationLogService;

    // Test 생성
    @PostMapping("create/{schemaName}")
    public ResponseEntity create(@PathVariable String schemaName, @RequestBody ConversationLog conversationLog) {
        TenantContext.setCurrentTenant(schemaName);
        var result = conversationLogService.create(conversationLog);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 내역 생성 완료").data(result).build());
    }
}
