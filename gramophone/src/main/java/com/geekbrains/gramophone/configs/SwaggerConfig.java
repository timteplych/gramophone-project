package com.geekbrains.gramophone.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collection;
import java.util.Collections;

// http://localhost:8189/gramophone/swagger-ui.html#/test-controller

@Configuration
@EnableSwagger2
@ComponentScan("com.geekbrains.gramophone.rest")
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.geekbrains.gramophone.rest"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {

        Contact contact = new Contact("Anton", "", "atoty@mail.ru");
        Collection<VendorExtension> vendorExtensions = Collections.emptyList();

        return new ApiInfo(
                "REST API project Gramophone",
                "Some information",
                "1.0",
                "com.geekbrains.gramophone.rest",
                contact,
                "no",
                "",
                vendorExtensions
                );
    }

}
