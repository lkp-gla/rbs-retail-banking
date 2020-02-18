package com.hiring.rbs.banking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@PropertySource(value = {"classpath:META-INF/build-info.properties"})
public class SwaggerConfig {
    private String email;
    private String user;
    private String version;

    @Value("{git.build.user.email}")
    public void setEmail(String email) {
        this.email = email;
    }

    @Value("{git.build.user.name}")
    public void setUser(String user) {
        this.user = user;
    }

    @Value("{build.version}")
    public void setVersion(String version) {
        this.version = version;
    }

    @Bean
    public Docket api(@Autowired final ApiInfo apiInfo) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hiring.rbs.banking.controller"))
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .apiInfo(apiInfo);
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfo("RBS Basic Retail Banking ",
                "REST API for com.hiring.rbs.banking services",
                version,
                "",
                user,
                "",
                ""
        );
    }
}
