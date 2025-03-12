package com.cmyk.ego.speaktoyouspring.config;

import com.cmyk.ego.speaktoyouspring.config.properties.MetadataProperties;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

/// MetaData Database에 관한 기능을 명시하는 Configuration 클래스이다.
@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
        // metadata 패키지 하위의 @Repository들을 대상으로 설정
        basePackages = MetadataConfig.BASE_PACKAGE,
        entityManagerFactoryRef = "metadataEntityManagerFactory", // 엔티티를 조작할 Manager를 생성
        transactionManagerRef = "metadataTransactionManager") // (클래스 내부에 작성되어 있는 함수명)
public class MetadataConfig {
    // 파일 내에서만 url을 사용 가능하도록 Default 접근 지정자 설정
    static final String BASE_PACKAGE = "com.cmyk.ego.speaktoyouspring.api.metadata";

    private final MetadataProperties metadataProperties;

    /// DataSource와 관련된 Bean이 다양한데, Bean 할당 순서를 수동으로 조정하기 위함이다.
    /// DataSource를 사용하는 다른 클래스에선 @Qualifier를 사용한다.
    ///
    /// metadata 데이터베이스에 대한 데이터 소스를 저장한다.
    @Primary
    @Bean
    public DataSource metadataDataSource() {
        return metadataProperties.getDatasource();
    }

    /// 엔티티를 조작할 Manager를 생성
    ///
    /// metadataTransactionManager()에서도 활용하는 것을 보면 아마 Factory의 의미가 추상 팩토리 패턴인 것 같음
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean metadataEntityManagerFactory(
            EntityManagerFactoryBuilder builder) { // 의존관계 역전 시킨 builder(여기에 우리가 추가로 설정을 부여하는 것)
        return builder
                .dataSource(metadataDataSource()) // 데이터 소스 추가
                // 조작할 클래스(경로 하위에 있는 @Repository) 명시
                .packages(MetadataConfig.BASE_PACKAGE)
                .persistenceUnit("metaDB") // 여러 개의 EntityManagerFactory를 구분하기 위한 논리적인 식별자
                .build();
    }

    /// 트랜잭션을 관리하기 위한 도구
    @Primary
    @Bean
    public JpaTransactionManager metadataTransactionManager(
            // 의존관계 역전 시킨 metadataEntityManagerFactory(여기에 우리가 추가로 설정을 부여하는 것)
            @Qualifier("metadataEntityManagerFactory") EntityManagerFactory metadataEntityManagerFactory) {
        return new JpaTransactionManager(metadataEntityManagerFactory);
    }
}

