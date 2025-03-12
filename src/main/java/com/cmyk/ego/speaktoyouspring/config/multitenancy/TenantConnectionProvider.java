package com.cmyk.ego.speaktoyouspring.config.multitenancy;

import lombok.extern.log4j.Log4j2;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/// 실제로 DB에 SET SCHEMA 명령을 내려주는 클래스이다.
/// 테넌트 마다 지정된 DB 커넥션을 제공하는 역할이다.
@Component
@Log4j2
public class TenantConnectionProvider implements MultiTenantConnectionProvider {
    private final DataSource dataSource;

    /// DataSource를 불러오는 함수
    public TenantConnectionProvider(@Qualifier("tenantDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /// 어떠한 커넥션을 가져오는 함수
    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /// 커넥션을 제거하는 함수
    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /// 테넌트의 스키마를 설정한 후 커넥션을 반환하는 함수
    @Override
    public Connection getConnection(Object tenantName) throws SQLException {
        final Connection connection = getAnyConnection();
        // 지정한 스키마 값 할당
        connection.setSchema((String)tenantName);
        return connection;
    }

    /// 커넥션을 주어진 테넌트의 스키마로 설정한 후 닫는 함수
    @Override
    public void releaseConnection(Object tenantName, Connection connection) throws SQLException {
        connection.setSchema((String)tenantName);
        releaseAnyConnection(connection);
    }

    /// Hibernate가 커넥션을 적극적으로 반환할지를 설정하는 함수 (false로 설정하여 관리)
    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }


    /// 주어진 클래스 타입으로 언래핑할 수 있는지 여부를 반환하는 함수
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    /// 특정 클래스 타입으로 언래핑할 때 사용하는 함수 (여기서는 지원하지 않음)
    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
