package com.masteranything.security.config.shared;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

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
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
