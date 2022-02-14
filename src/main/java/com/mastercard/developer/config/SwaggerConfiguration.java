package com.mastercard.developer.config;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket apis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .produces(Sets.newHashSet(APPLICATION_JSON_VALUE))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mastercard.developer.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Promotions Digital Enablement Reference Application")
                .description("This is a reference application that demonstrates how clients can consume Promotions Digital Enablement API which support different promotions activities to Mastercard Network.")
                .termsOfServiceUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}