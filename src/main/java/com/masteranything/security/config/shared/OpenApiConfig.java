package com.masteranything.security.config.shared;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;

/**
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                name = "MasterAnything",
                email = "stephane.nganou.w@snganou.de"
            ),
            description = "OpenApi documentation for this Spring security",
            title = "OpenApi specification - MasterAnything",
            version = "1.0",
            license = @License(
                name = "License name",
                url = "http://.snganou.de"
            ),
            termsOfService = "Terms of Service"
        ),
        servers = {
            @Server(
                description = "Local ENV",
                url = "http://localhost:8088/api/v1"
            )
        },
        security = {
            @SecurityRequirement(
                name = "bearerAuth"
            )
        }
) */
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
            .info(
                new io.swagger.v3.oas.models.info.Info()
                .contact(
                    new io.swagger.v3.oas.models.info.Contact()
                        .name("MasterAnything")
                        .email("stephane.nganou.w@snganou.de")
                )
                .description("OpenApi documentation for this Spring security")
                .title("OpenApi specification - MasterAnything")
                .license(
                    new io.swagger.v3.oas.models.info.License()
                    .name("License name")
                    .url("http://.snganou.de")
                )
                .termsOfService(null)

            )
            .servers(
                List.of(
                    new io.swagger.v3.oas.models.servers.Server()
                    .description("Local ENV")
                    .url("http://localhost:8088/api/v1")
                )
            )
            .security(
                List.of(
                    new io.swagger.v3.oas.models.security.SecurityRequirement()
                    .addList("bearerAuth")
                )
            );
    }
}
