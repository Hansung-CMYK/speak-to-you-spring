package com.cmyk.ego.speaktoyouspring.api.admin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "admin.db")
public class AdminDbProperties {
    private String host;
    private String port;
    private String user;
    private String password;
    private String dbA;
    private String dbB;
    private String linuxBackupDir;
    private String windowsBackupDir;
    private String windowsPostgresPath;
}

