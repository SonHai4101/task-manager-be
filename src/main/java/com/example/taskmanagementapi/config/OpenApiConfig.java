package com.example.taskmanagementapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Configuration
@JsonPropertyOrder(alphabetic = true)
@OpenAPIDefinition(
        info = @Info(
                title = "Task Management API",
                version = "1.0",
                description = "JWT secured API"
        ),
        security = @SecurityRequirement(name = "bearerAuth"),
        tags = {
                @Tag(name = "1. User"),
                @Tag(name = "2. Auth"),
                @Tag(name = "3. Admin"),
                @Tag(name = "4. Tasks"),
                @Tag(name = "5. Audit log"),
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
        @Bean
        public OpenAPI pageableOpenAPI() {
                return new OpenAPI()
                        .components(
                                new Components()
                                        .addParameters(
                                                "page",
                                                new Parameter()
                                                        .name("page")
                                                        .in("query")
                                                        .schema(new Schema<Integer>().type("integer")._default(0))
                                        )
                                        .addParameters(
                                                "size",
                                                new Parameter()
                                                        .name("size")
                                                        .in("query")
                                                        .schema(new Schema<Integer>().type("integer")._default(10))
                                        )
                                        .addParameters(
                                                "sort",
                                                new Parameter()
                                                        .name("sort")
                                                        .in("query")
                                                        .schema(new Schema<String>().type("string")._default("createdAt,desc"))
                                        )
                        );
        }
}
