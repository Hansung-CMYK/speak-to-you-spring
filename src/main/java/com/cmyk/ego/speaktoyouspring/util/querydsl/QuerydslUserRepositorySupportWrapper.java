package com.cmyk.ego.speaktoyouspring.util.querydsl;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/// 사용자 DB(EntityManager)를 사용하는 QueryDSL 지원 클래스
///
/// QuerydslRepositorySupportWrapper를 확장하여 사용자(User) 관련 데이터베이스에 연결하여 QueryDSL 실행 가능하도록 설정.
/// userEntityManagerFactory를 이용해 User 데이터베이스에 대한 QueryDSL 쿼리 지원.
@Repository
public abstract class QuerydslUserRepositorySupportWrapper extends QuerydslRepositorySupportWrapper {

  public QuerydslUserRepositorySupportWrapper(Class<?> domainClass) {
    super(domainClass);
  }

  @Autowired
  public void setEntityManager(@Qualifier("tenantEntityManagerFactory") EntityManager entityManager) {

    Assert.notNull(entityManager, "EntityManager must not be null!");
    this.querydsl = new Querydsl(entityManager, builder);
    this.entityManager = entityManager;
  }

}
