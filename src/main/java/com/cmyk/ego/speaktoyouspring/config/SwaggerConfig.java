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
                .title("Speak To You") // 스웨거에 나타날 제목
                .description("2025 한성대학교 캡스톤 디자인") // 해당 docs를 설명하는 내용
        );
    }
}
