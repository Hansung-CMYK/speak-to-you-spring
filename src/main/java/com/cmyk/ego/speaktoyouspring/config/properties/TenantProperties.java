package com.cmyk.ego.speaktoyouspring.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/// `apllication.yml`에 있는 정보를 가져오는 Propertie 클래스이다.
@Data
@ConfigurationProperties(prefix = "tenant.datasource")
public class TenantProperties {
    private String jdbcUrl; // 데이터베이스 경로
    private String username; // 사용자 이름
    private String password; // 비밀번호

    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}
