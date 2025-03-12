package com.cmyk.ego.speaktoyouspring.api.hub.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// 테넌트 레포지토리에 접근하기 위한 클래스이다.
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByTenantName(String tenantName);
}