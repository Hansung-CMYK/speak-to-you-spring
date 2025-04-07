package com.cmyk.ego.speaktoyouspring.api.tenant.conversationlog;

import com.cmyk.ego.speaktoyouspring.api.hub.tenant.TenantService;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversationLog/")
@RequiredArgsConstructor
public class ConversationLogController {

    @Autowired
    private final ConversationLogService conversationLogService;

    @Autowired
    private final TenantService tenantService;

    // Test 생성
    @PostMapping("create/{schemaName}")
    public ResponseEntity create(@PathVariable String schemaName, @RequestBody ConversationLog conversationLog) {
        TenantContext.setCurrentTenant(schemaName);
        var result = conversationLogService.create(conversationLog);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("대화 내역 생성 완료").data(result).build());
    }

    // conversation_log에 기록된 전체 topic반환
    @GetMapping("{schemaName}/conversation-logs")
    public ResponseEntity<?> readAll(@PathVariable String schemaName) {
        try {
            TenantContext.setCurrentTenant(schemaName);
            var result = conversationLogService.readAllActiveLogs();

            String msg = result.isEmpty() ? "대화한 주제가 없습니다." : "전체 주제 조회";

            return ResponseEntity.ok(
                    CommonResponse.builder()
                            .code(200)
                            .message(msg)
                            .data(result)
                            .build()
            );
        } catch (Exception e) { // Schema가 없는 경우
            e.printStackTrace();
            return ResponseEntity.status(404).body(
                    CommonResponse.builder()
                            .code(404)
                            .message("존재하지 않는 테넌트(스키마)입니다.")
                            .data(null)
                            .build()
            );
        } finally {
            TenantContext.clear();
        }
    }
}
