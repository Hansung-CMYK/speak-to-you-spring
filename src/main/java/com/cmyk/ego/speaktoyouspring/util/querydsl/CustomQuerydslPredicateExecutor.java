package com.cmyk.ego.speaktoyouspring.util.querydsl;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/// QueryDSL을 활용한 동적 쿼리 실행을 위한 인터페이스
///
/// QuerydslPredicateExecutor<T>를 확장하여 QueryDSL을 통해 다양한 방법으로 데이터를 조회할 수 있도록 하는 인터페이스.
/// JPQLQuery와 FactoryExpression을 활용하여 데이터를 조회하는 메서드 제공.
public interface CustomQuerydslPredicateExecutor<T> extends QuerydslPredicateExecutor<T> {
  <P> Optional<P> findOne(@NonNull JPQLQuery<P> query);

  <P> Optional<P> findOne(@NonNull FactoryExpression<P> factoryExpression,
                          @NonNull Predicate predicate);

  <P> List<P> findAll(@NonNull JPQLQuery<P> query);

  <P> Page<P> findAll(@NonNull JPQLQuery<P> query, @NonNull Pageable pageable);

  <P> List<P> findAll(@NonNull FactoryExpression<P> factoryExpression,
                      @NonNull Predicate predicate);

  <P> Page<P> findAll(@NonNull FactoryExpression<P> factoryExpression,
                      @NonNull Predicate predicate,
                      @NonNull Pageable pageable);
}
