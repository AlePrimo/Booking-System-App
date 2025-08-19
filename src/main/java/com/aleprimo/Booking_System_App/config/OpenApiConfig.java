package com.aleprimo.Booking_System_App.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookingSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Booking System API")
                        .description("API para gestión de reservas, notificaciones y pagos en un sistema de booking. " +
                                "Incluye soporte para usuarios, servicios ofrecidos, validaciones y paginación.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Alejandro Primo")
                                .email("tuemail@example.com")
                                .url("https://github.com/AlePrimo"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio en GitHub")
                        .url("https://github.com/AlePrimo/Booking-System-App"));
    }
}
