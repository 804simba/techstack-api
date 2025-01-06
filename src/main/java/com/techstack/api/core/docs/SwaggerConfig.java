package com.techstack.api.core.docs;

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
                contact = @Contact(name = "Timothy Ngonadi", email = "timothyolisaeloka@gmail.com", url = "https://www.github.com/804simba"),
                description = "Open API documentation for TechStack.",
                title = "Techstack Backend API",
                version = "1.0",
                license = @License(name = "Apache License", url = "https://www.apache.org/licenses/LICENSE-2"),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "DEV ENV",
                        url = "http://localhost:8080"
                )
        }
)
public class SwaggerConfig {
}
