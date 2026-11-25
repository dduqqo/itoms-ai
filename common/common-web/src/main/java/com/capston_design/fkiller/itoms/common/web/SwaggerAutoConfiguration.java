package com.capston_design.fkiller.itoms.common.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 서비스별 Swagger 문서 정보. 각 서비스 application.yml의 itoms.swagger.title, itoms.swagger.description으로 정한다.
 */
@AutoConfiguration
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openAPI(@Value("${itoms.swagger.title:${spring.application.name:ITOMS} API Document}") String title,
                           @Value("${itoms.swagger.description:}") String description) {
        return new OpenAPI().info(new Info().title(title).version("1.0").description(description));
    }
}
