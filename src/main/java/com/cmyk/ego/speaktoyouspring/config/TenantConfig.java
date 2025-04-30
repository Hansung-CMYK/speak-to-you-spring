package com.cmyk.ego.speaktoyouspring.config;

import com.cmyk.ego.speaktoyouspring.config.properties.PersonalizedDataProperties;
import com.cmyk.ego.speaktoyouspring.util.querydsl.QuerydslJpaRepositoryFactoryBean;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/// Tenant Database에 관한 기능을 명시하는 Configuration 클래스이다.
///
/// 해당 코드는 두개의 데이터베이스를 이용하기 위한 작업의 일환이다.
@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
    repositoryFactoryBeanClass = QuerydslJpaRepositoryFactoryBean.class,
    basePackages = TenantConfig.BASE_PACKAGE, // Talk 클래스를 레포지토리 대상으로 설정
    entityManagerFactoryRef = "tenantEntityManagerFactory", // (클래스 내부에 작성되어 있는 함수명)
    transactionManagerRef = "tenantTransactionManager" // (클래스 내부에 작성되어 있는 함수명)
)
public class TenantConfig {
    // 파일 내에서만 url을 사용 가능하도록 Default 접근 지정자 설정
    static final String BASE_PACKAGE = "com.cmyk.ego.speaktoyouspring.api.tenant";
    private final JpaProperties jpaProperties; // TODO: ???
    private final PersonalizedDataProperties personalizedDataProperties;

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    /// personalized-data 데이터베이스에 대한 데이터 소스를 저장한다.
    @Bean
    public DataSource personalizedDataSource() {
        return personalizedDataProperties.getDataSource();
    }

    /// 엔티티를 조작할 Manager를 생성
    ///
    /// tenantTransactionManager()에서도 활용하는 것을 보면 아마 Factory의 의미가 추상 팩토리 패턴인 것 같음
    @Bean
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
            MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
            CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl
    ) {
        // JPA 프로퍼티 설정
        Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());

        // 멀티 테넌트 관련 설정 추가
        // 테넌트별로 다른 데이터 소스를 제공 한다는 의미
        jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
        // 현재 요청이 어떤 테넌트에 속하는지 결정한다는 의미
        jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);

        // LocalContainerEntityManagerFactoryBean 설정
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(personalizedDataSource()); // personalized-data의 데이터 소스 설정
        em.setPackagesToScan(BASE_PACKAGE); // 엔티티 클래스가 포함된 패키지를 스캔
        em.setJpaVendorAdapter(jpaVendorAdapter()); // JPA 구현체 설정 (Hibernate)
        em.setJpaPropertyMap(jpaPropertiesMap); // JPA 속성 설정
        em.setPersistenceUnitName("tenantDB"); // Persistence Unit 이름 설정
        return em;
    }

    /// 트랜잭션을 관리하기 위한 도구
    @Bean
    public JpaTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManagerFactory) {
        return new JpaTransactionManager(tenantEntityManagerFactory);
    }
}