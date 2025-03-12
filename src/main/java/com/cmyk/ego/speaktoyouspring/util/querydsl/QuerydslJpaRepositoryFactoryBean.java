package com.cmyk.ego.speaktoyouspring.util.querydsl;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/// QueryDSL을 활용한 Repository Factory Bean
///
/// Spring Data JPA의 기본 Repository Factory (JpaRepositoryFactoryBean)를 확장하여 QueryDSL을 지원하는 커스텀 Repository Factory를 생성.
/// JpaRepositoryFactory를 확장하여 QueryDSL 기반의 QuerydslJpaBaseRepository를 사용하도록 설정.
public class QuerydslJpaRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, ID> {

  public QuerydslJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  @NonNull
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new QuerydslJpaRepositoryFactory(entityManager);
  }

  private static class QuerydslJpaRepositoryFactory extends JpaRepositoryFactory {
    QuerydslJpaRepositoryFactory(EntityManager entityManager) {
      super(entityManager);
    }

    @Override
    @NonNull
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,
                                                                    @NonNull EntityManager entityManager) {
      if (isQueryDslRepository(information)) {
        return new QuerydslJpaBaseRepository<>(getEntityInformation(information.getDomainType()), entityManager);
      }
      return super.getTargetRepository(information, entityManager);
    }

    @Override
    @NonNull
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      if (isQueryDslRepository(metadata)) {
        return QuerydslJpaBaseRepository.class;
      }
      return super.getRepositoryBaseClass(metadata);
    }

    private boolean isQueryDslRepository(RepositoryMetadata metadata) {
      Class<?> repositoryInterface = metadata.getRepositoryInterface();
      return CustomQuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }
  }
}
