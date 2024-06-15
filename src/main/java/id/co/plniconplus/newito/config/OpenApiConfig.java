package id.co.plniconplus.newito.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Backend New ITO", version = "1.0",
        contact = @Contact(name = "newito.pln.co.id",
                email = "newito@pln.co.id",
                url = "https://newito.pln.co.id"),
        description = "REST API Documentation for New ITO"),
        security = @SecurityRequirement(name = "bearerToken")
)
@SecuritySchemes(
        @SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
)
public class OpenApiConfig {
}
