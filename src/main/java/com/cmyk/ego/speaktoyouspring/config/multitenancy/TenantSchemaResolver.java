package com.cmyk.ego.speaktoyouspring.config.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import static com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext.DEFAULT_SCHEMA;

/// 현재 스레드가 접근할 스키마 명을 지정하는 클래스
@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

  /// 현재 테넌트의 스키마를 설정
  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenantUUID = TenantContext.getCurrentTenant();

    return tenantUUID != null ? tenantUUID : DEFAULT_SCHEMA;
  }

  /// 테넌트 식별자가 변경될 수 있는 환경에서 Hibernate가 기존 세션을 유지해야 하는지 여부
  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
