package com.example.restapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI farmaciaOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                    .title("Farmacia REST API")
                    .description("Backend para gestión de medicamentos, carrito y compras")
                    .version("v1.0.0")
                    .contact(new Contact()
                            .name("Equipo Farmacia")
                            .email("soporte@farmacia-demo.es"))
                    .license(new License()
                            .name("MIT")
                            .url("https://opensource.org/licenses/MIT")))
            .externalDocs(new ExternalDocumentation()
                    .description("Repositorio Git")
                    .url("https://github.com/tu-org/farmacia-api"));
    }
}
