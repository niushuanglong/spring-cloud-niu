package com.niu.study;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.niu.study"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Build api document detail info
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("UI Client")
                .description("UI Client REST API")
                .version("1.0.0")
                .build();
    }

}
