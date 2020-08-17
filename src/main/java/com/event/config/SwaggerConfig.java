package com.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket newsApi() {
        Docket docket = new Docket(SWAGGER_2);
        docket.enable(true);
        docket.select().paths(PathSelectors.any()).build();
        return docket;
    }
}
