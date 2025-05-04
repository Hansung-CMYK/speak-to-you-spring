package com.cmyk.ego.speaktoyouspring.config.flyway;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccount;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountDTO;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountService;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserRequestBody;
import com.cmyk.ego.speaktoyouspring.config.properties.PersonalizedDataProperties;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/// Flyway 기능을 명시하는 Service 클래스이다.
@Service
@EnableConfigurationProperties(FlywayProperties.class)
@RequiredArgsConstructor
public class FlywayService {
    private final PersonalizedDataProperties personalizedDataProperties;
    private final FlywayProperties flywayProperties;

    private final UserAccountService userAccountService;

    /**
     * personalized-data Database에 새로운 schema를 생성하는 함수
     * @param userAccountDTO : 새로 생성할 schema 정보를 가지고 있는 객체
     * @return UserAccount : 생성된 UserAccount 객체
     * */
    public UserAccount createUserSchema(UserAccountDTO userAccountDTO) {

        if(userAccountDTO.getCreatedAt() == null){
            userAccountDTO.setCreatedAt(LocalDate.now());
        }

        if(userAccountDTO.getIsDeleted() == null){
            userAccountDTO.setIsDeleted(false);
        }

        Flyway.configure()
                .dataSource(personalizedDataProperties.getDataSource())
                .locations(flywayProperties.getLocations().get(1)) // application.yml에서 경로 로드
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate()) // 기존 DB를 기준점으로 설정할지 여부
                .schemas(userAccountDTO.getUid())
                .load()
                .migrate();

        UserAccount userAccountEntity = userAccountDTO.toEntity();

        userAccountService.create(userAccountEntity);  // hub.user_account 테이블에 테넌트 정보 저장
        return userAccountEntity;
    }

}
