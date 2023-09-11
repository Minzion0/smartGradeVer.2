package com.green.smartgradever2.settings.security.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "SmartGradeVer.2", description = "SmartGrade 학사관리 시스템", version = "v2.0.0"),
        security = @SecurityRequirement(name = "Authorization")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "Authorization",
        scheme = "Bearer"
)
public class SwaggerConfiguration {// 개발단계에서 스웨거 확인을 위한 설정 클래스
//    private final String JWT_TYPE;
//    private final String HEADER_SCHEME_NAME;
//
//    public SwaggerConfiguration(@Value("${springboot.jwt.token-type}") String jwtType
//            , @Value("${springboot.jwt.header-scheme-name}") String headerSchemeName) {
//        this.JWT_TYPE = jwtType;
//        this.HEADER_SCHEME_NAME = headerSchemeName;
//    }
//
//    @Bean
//    public OpenAPI openAPI() {
//        final Info info = new Info()
//                .version("v0.0.1")
//                .title("리스트")
//                .description("Spring Security Exam");
//
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(HEADER_SCHEME_NAME);
//
//        SecurityScheme securityScheme = new SecurityScheme()
//                .name(HEADER_SCHEME_NAME)
//                .type(SecurityScheme.Type.HTTP)
//                .scheme(JWT_TYPE)
//                .in(SecurityScheme.In.HEADER)
//                .bearerFormat("JWT");
//
//        Components components = new Components()
//                .addSecuritySchemes(HEADER_SCHEME_NAME, securityScheme);
//
//        // SecuritySchemes 등록
//        return new OpenAPI()
//                .info(info)
//                .addSecurityItem(securityRequirement)
//                .components(components);
//    }
}
