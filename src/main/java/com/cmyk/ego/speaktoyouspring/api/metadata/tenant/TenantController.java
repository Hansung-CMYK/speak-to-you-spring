package com.cmyk.ego.speaktoyouspring.api.metadata.tenant;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.flyway.FlywayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenant/")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;
    private final FlywayService flywayService;

    @PostMapping("create/{tenantName}")
    public ResponseEntity create(@PathVariable String tenantName) {
        var result = flywayService.createTenant(tenantName);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("스키마 생성 완료").data(result).build());
    }

    @GetMapping("read/all")
    public ResponseEntity readAll() {
        var result = tenantService.getTenantName();

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("목록 조회 완료").data(result).build());
    }

    @DeleteMapping("delete/{schemaName}")
    public ResponseEntity delete(@PathVariable String schemaName) {
        var result = flywayService.deleteTenant(schemaName);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("스키마 제거 완료").data(result).build());
    }
}
