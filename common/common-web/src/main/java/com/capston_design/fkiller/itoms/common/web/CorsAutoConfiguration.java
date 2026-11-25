package com.capston_design.fkiller.itoms.common.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * cors.allowed.path가 설정된 서비스에만 CORS를 적용한다.
 * pattern, method는 쉼표로 여러 값을 줄 수 있다. 예: GET,POST,PATCH
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "cors.allowed", name = "path")
public class CorsAutoConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer(@Value("${cors.allowed.path}") String path,
                                           @Value("${cors.allowed.pattern}") String[] patterns,
                                           @Value("${cors.allowed.method}") String[] methods) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(path)
                        .allowedOriginPatterns(patterns)
                        .allowedMethods(methods)
                        .allowCredentials(true);
            }
        };
    }
}
