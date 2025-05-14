package com.cmyk.ego.speaktoyouspring.api.flyway;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountService;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.properties.HubProperties;
import com.cmyk.ego.speaktoyouspring.config.properties.PersonalizedDataProperties;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/migrate")
@RequiredArgsConstructor
@Validated
public class FlywayController {
    private final PersonalizedDataProperties personalizedDataProperties;
    private final HubProperties hubProperties;
    private final FlywayProperties flywayProperties;

    private final UserAccountService userAccountService;

    @PostMapping("/")
    public ResponseEntity migrateFlyway() {
        // hub Database에 관한 정보를 명시한다.
        Flyway.configure()
                .dataSource(hubProperties.getDatasource())
                .locations(flywayProperties.getLocations().getFirst()) // application.yml에서 경로 로드
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate()) // 기존 DB를 기준점으로 설정할지 여부
                .load()
                .migrate();

        // 모든 스키마 정보(uid값)를 얻는다.
        // 현재 personalized-data Database의 개별 스키마는 hub.user_account.uid로 등록되기 때문이다.
        final Set<String> schemas = userAccountService.getAllUserUID();

        // personalized-data Database에 관한 정보를 명시한다.
        schemas.forEach(schema -> {
            Flyway.configure()
                    .dataSource(personalizedDataProperties.getDataSource())
                    .locations(flywayProperties.getLocations().get(1))
                    .baselineOnMigrate(flywayProperties.isBaselineOnMigrate()) // 기존 DB를 기준점으로 설정할지 여부
                    .defaultSchema(schema) // 개별 테넌트 스키마 이름 적용 (uid로 적용됨)
                    .load()
                    .migrate(); // 개별 테넌트에 대해 마이그레이션 실행 ///
        });

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("Migrate 완료").build());
    }
}
