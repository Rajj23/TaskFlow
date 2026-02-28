package com.taskflow.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        private static final String SECURITY_SCHEME_NAME = "BearerAuth";

        @Bean
        public OpenAPI taskFlowOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("TaskFlow API")
                                                .description(
                                                                "Task Management backend REST API. " +
                                                                                "All endpoints except `/auth/**` require a valid JWT Bearer token. "
                                                                                +
                                                                                "Use **POST /auth/login** to obtain a token, then click **Authorize** above.")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("TaskFlow Team")
                                                                .url("https://github.com/Rajj23/TaskManagment_Backend")))
                                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                                .components(new Components()
                                                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                                                new SecurityScheme()
                                                                                .name(SECURITY_SCHEME_NAME)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .description("Enter your JWT token (without the 'Bearer ' prefix)")));
        }
}
