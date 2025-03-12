package com.cmyk.ego.speaktoyouspring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// 스웨거에 대한 정보를 명시하는 Configuration 클래스이다.
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(
            new Info()
                .version("v1.0.0") // 스웨거에 나타날 버전 정보
                .title("다중 멀티테넌시 구현 레포지토리") // 스웨거에 나타날 제목
                .description("다중 멀티테넌시 API 문서") // 해당 docs를 설명하는 내용
        );
    }
}
