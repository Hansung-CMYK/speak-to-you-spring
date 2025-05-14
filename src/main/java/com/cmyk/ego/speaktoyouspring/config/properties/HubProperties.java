package com.cmyk.ego.speaktoyouspring.config.properties;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/// `apllication.yml`에 있는 정보를 가져오는 Propertie 클래스이다.
@Data
@ConfigurationProperties(prefix = "hub.datasource")
public class HubProperties {
    private String jdbcUrl; // 데이터베이스 경로
    private String username; // 사용자 이름
    private String password; // 비밀번호

    public DataSource getDatasource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setMaximumPoolSize(10);
        ds.setMinimumIdle(1);
        ds.setIdleTimeout(10000);
        ds.setMaxLifetime(30000);
        ds.setConnectionTimeout(5000);
        return ds;
    }
}
