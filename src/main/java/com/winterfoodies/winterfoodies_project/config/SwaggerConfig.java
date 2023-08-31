package com.winterfoodies.winterfoodies_project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig { // 스웨거 버전 3.0.0
    @Bean
    public Docket api() {


        return new Docket(DocumentationType.OAS_30)
                // 헤더 설정을 위해 3줄 추가 - 230831
                .useDefaultResponseMessages(false) //Swagger에서 제공해주는 기본 응답 코드 받지 않음 설정
                .securityContexts(List.of(this.securityContext())) // SecurityContext 설정
                .securitySchemes(List.of(this.apiKey())) // ApiKey 설정
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }


    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Winterfoodies API")
                .description("윈터푸디스 API 명세서")
                .version("0.1")
                .build();
    }

    // 헤더 설정을 위해 추가 - 230831

    // JWT SecurityContext 구성
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }

    //Authorization을 인증 헤더로 포함하도록 ApiKey를 정의
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }


}