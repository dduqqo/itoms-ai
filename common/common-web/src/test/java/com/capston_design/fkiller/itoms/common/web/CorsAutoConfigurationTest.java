package com.capston_design.fkiller.itoms.common.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

class CorsAutoConfigurationTest {

    private final WebApplicationContextRunner runner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CorsAutoConfiguration.class));

    @Test
    void cors_설정이_있으면_적용한다() {
        runner.withPropertyValues("cors.allowed.path=/**", "cors.allowed.pattern=http://localhost:*",
                        "cors.allowed.method=GET,POST")
                .run(context -> assertThat(context).hasSingleBean(WebMvcConfigurer.class));
    }

    @Test
    void cors_설정이_없으면_적용하지_않는다() {
        runner.run(context -> assertThat(context).doesNotHaveBean(WebMvcConfigurer.class));
    }
}
