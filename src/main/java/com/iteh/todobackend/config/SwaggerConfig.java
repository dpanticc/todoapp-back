package com.iteh.todobackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@ComponentScan("com.iteh.todobackend.config")
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iteh.todobackend.controller"))
                .apis(RequestHandlerSelectors.basePackage("com.iteh.todobackend.auth"))
                .paths(PathSelectors.any())
                .build();
    }
}
