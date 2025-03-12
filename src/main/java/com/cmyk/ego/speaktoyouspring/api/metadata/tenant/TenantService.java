package com.cmyk.ego.speaktoyouspring.api.metadata.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/// 테넌트를 관리할 수 있게 만들어둔 클래스이다.
@Service
@Transactional
@RequiredArgsConstructor
public class TenantService {
  private final TenantRepository tenantRepository;

  /// 스키마의 이름을 반환하는 함수
  public Set<String> getTenantName() {
    return tenantRepository.findAll().stream().map(Tenant::getTenantName).collect(Collectors.toSet());
  }

  /// 파라미터 `tenant`를 데이터 베이스에 저장(생성)하는 함수
  public Tenant create(Tenant tenant) {
    return tenantRepository.save(tenant);
  }

  public Tenant delete(String tenantName) {
    var tenant = tenantRepository.findByTenantName(tenantName)
            .orElseThrow(RuntimeException::new);
    tenantRepository.delete(tenant);
    return tenant;
  }
}
