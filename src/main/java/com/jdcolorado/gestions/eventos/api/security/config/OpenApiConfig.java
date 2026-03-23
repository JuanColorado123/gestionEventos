package com.jdcolorado.gestions.eventos.api.security.config;

import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final String SCHEMA_NAME = "bearerAuth";
    private final String BEARER_FORMAT = "JWT";
    private final String DESCRIPTION = "JWT Authentication para la API de eventos";
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SCHEMA_NAME))
                .components(new Components()
                        .addSecuritySchemes(SCHEMA_NAME,
                                new SecurityScheme()
                                        .name(SCHEMA_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat(BEARER_FORMAT)
                                        .in(SecurityScheme.In.HEADER)
                                        .description(DESCRIPTION)))
                .info(new Info()
                        .title("Gestion de eventos API")
                        .version("1.0.0")
                        .description("API RESTFULL para la gestion eventos")
                        .contact(new Contact()
                                .name("Juan David")
                                .email("juandavidcolorado123@gmail.com")
                                .url("https://tuportafolio.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.htm")
                ));
    }
}
