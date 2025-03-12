package com.cmyk.ego.speaktoyouspring.util.querydsl;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/// 테넌트(EntityManager)를 사용하는 QueryDSL 지원 클래스
///
/// QuerydslRepositorySupportWrapper를 확장하여 멀티 테넌트 환경에서 사용할 수 있도록 tenantEntityManagerFactory를 이용하여 설정.
/// EntityManager를 테넌트 DB에 연결하여 실행.
@Repository
public abstract class QuerydslTenantRepositorySupportWrapper extends QuerydslRepositorySupportWrapper {

  public QuerydslTenantRepositorySupportWrapper(Class<?> domainClass) {
    super(domainClass);
  }

  @Autowired
  public void setEntityManager(@Qualifier("tenantEntityManagerFactory") EntityManager entityManager) {

    Assert.notNull(entityManager, "EntityManager must not be null!");
    this.querydsl = new Querydsl(entityManager, builder);
    this.entityManager = entityManager;
  }

}
