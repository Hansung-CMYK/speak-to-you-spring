package com.cmyk.ego.speaktoyouspring.config.flyway;

import com.cmyk.ego.speaktoyouspring.api.metadata.tenant.Tenant;
import com.cmyk.ego.speaktoyouspring.api.metadata.tenant.TenantService;
import com.cmyk.ego.speaktoyouspring.config.properties.TenantProperties;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/// Flyway 기능을 명시하는 Service 클래스이다.
@Service
@EnableConfigurationProperties(FlywayProperties.class)
@RequiredArgsConstructor
public class FlywayService {
    private final TenantProperties tenantProperties;
    private final FlywayProperties flywayProperties;

    private final TenantService tenantService;

    /// tenant Database에 새로운 schema를 생성하는 함수
    /// @param tenantName 새로 생성할 schema 이름
    public Tenant createTenant(String tenantName) {
        Flyway.configure()
                .dataSource(tenantProperties.getDataSource())
                .locations(flywayProperties.getLocations().get(1)) // application.yml에서 경로 로드
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate()) // 기존 DB를 기준점으로 설정할지 여부
                .schemas(tenantName)
                .load()
                .migrate();

        // 생성된 tenant.{schemaName} Schema 정보를 metadata.public.tenant Table에 저장
        Tenant tenant = Tenant.builder()
            .tenantName(tenantName)
            .build();

        tenantService.create(tenant);  // metadata.tenant 테이블에 테넌트 정보 저장
        return tenant;
    }

    /// TODO: 직접적으로 운영 환경에서 clean 함수를 사용하는 것은 권장되지 않는 행위임
    /// TODO: 권한 등의 대책을 마련할 필요가 있다.
    public Tenant deleteTenant(String tenantName) {
        Flyway.configure()
                .dataSource(tenantProperties.getDataSource())
                .locations(flywayProperties.getLocations().get(1))
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate())
                .cleanDisabled(false)
                .schemas(tenantName)
                .load()
                .clean();

        return tenantService.delete(tenantName);
    }
}
