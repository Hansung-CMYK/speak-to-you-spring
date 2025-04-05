package com.cmyk.ego.speaktoyouspring.config;

import com.cmyk.ego.speaktoyouspring.api.hub.tenant.TenantService;
import com.cmyk.ego.speaktoyouspring.config.properties.HubProperties;
import com.cmyk.ego.speaktoyouspring.config.properties.TenantProperties;
import jakarta.activation.DataSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/// Flyway와 관련된 정보를 명시하는 Configuration이다.
@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
@RequiredArgsConstructor
public class FlywayConfig {
    private final TenantProperties tenantProperties;
    private final HubProperties hubProperties;
    private final FlywayProperties flywayProperties;

    private final TenantService tenantService;

    /// metadata와 tenant Database를 마이그레이션(형상 관리) 하는 함수.
    @PostConstruct
    public void migrateFlyway() {
        // metadata Database에 관한 정보를 명시한다.
        Flyway.configure()
                .dataSource(hubProperties.getDatasource())
                .locations("resources/db/migration/hub")
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate()) // 기존 DB를 기준점으로 설정할지 여부
                .load()
                .migrate();

        // 모든 스키마 정보(스키마 이름)를 얻는다.
        // 현재 tenant Database의 개별 스키마는 metadata.tenant.schemaName으로 등록되기 때문이다.
        final Set<String> schemas = tenantService.getTenantName();

        // tenant Database에 관한 정보를 명시한다.
        schemas.forEach(schema -> {
            Flyway.configure()
                    .dataSource(tenantProperties.getDataSource())
                    .locations(flywayProperties.getLocations().get(1))
                    .baselineOnMigrate(flywayProperties.isBaselineOnMigrate()) // 기존 DB를 기준점으로 설정할지 여부
                    .defaultSchema(schema) // 개별 테넌트 스키마 이름 적용
                    .load()
                    .migrate(); // 개별 테넌트에 대해 마이그레이션 실행
        });
    }
}
