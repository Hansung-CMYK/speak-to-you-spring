package com.cmyk.ego.speaktoyouspring.util.querydsl;

import com.cmyk.ego.speaktoyouspring.config.flyway.FlywayService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// 멀티 테넌트 환경에서 스키마(데이터베이스)를 초기화 및 관리하는 서비스
///
/// 테넌트 스키마 생성 및 초기화 (initSchemas)
/// 기존 테넌트 스키마 삭제 (rollbackSchema)
/// SQL 실행 (execute)
/// Flyway를 사용하여 테넌트의 마이그레이션(데이터베이스 버전 관리) 실행
@Service
@RequiredArgsConstructor
@Transactional(value = "tenantTransactionManager")
public class TenantDatabaseService {

    @Qualifier("tenantEntityManagerFactory")
    private final EntityManager tenantEntityManager;

    private final FlywayService flywayService;

//    @SneakyThrows
//    public void initSchemas(List<String> userAccount) {
//        userAccount.forEach(
//                userAccountUID -> {
//                    rollbackSchema(userAccountUID);
//                    flywayService.createUserSchema(userAccountUID);
//                });
//    }

    public void rollbackSchema(String schemaName) {
        execute("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE");
    }

    public void execute(String sql) {
        tenantEntityManager.createNativeQuery(sql).executeUpdate();
    }
}
