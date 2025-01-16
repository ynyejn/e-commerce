package kr.hhplus.be.server.support.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import kr.hhplus.be.server.support.auth.AuthenticatedUser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String USER_ID_HEADER = "USER-ID";

    @Bean
    public OpenAPI EcommerceAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(USER_ID_HEADER,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name(USER_ID_HEADER)
                                        .description("사용자 인증을 위한 ID값")))
                .info(new Info()
                        .title("Ecommerce API")
                        .version("v0.0.1")
                        .description("이커머스 API 문서 - 사용자 인증이 필요한 API는 USER-ID 헤더가 필요합니다.")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }

    @Bean
    public OperationCustomizer authHeaderCustomizer() {
        return (operation, handlerMethod) -> {
            boolean requiresAuthentication = java.util.Arrays.stream(handlerMethod.getMethodParameters())
                    .anyMatch(parameter -> parameter.hasParameterAnnotation(AuthenticatedUser.class));

            if (requiresAuthentication) {
                Parameter userIdHeader = new Parameter()
                        .in("header")
                        .name(USER_ID_HEADER)
                        .description("사용자 인증을 위한 ID값 (필수)")
                        .required(true)
                        .schema(new io.swagger.v3.oas.models.media.StringSchema());

                operation.addParametersItem(userIdHeader);
            }

            return operation;
        };
    }
}
