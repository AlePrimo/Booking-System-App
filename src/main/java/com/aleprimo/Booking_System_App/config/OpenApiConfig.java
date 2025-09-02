package com.aleprimo.Booking_System_App.config;

import io.swagger.v3.oas.models.parameters.Parameter;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;




@Configuration
public class OpenApiConfig {
//    http://localhost:8080/swagger-ui/index.html

    @Bean
    public OpenAPI bookingSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Booking System API")
                        .description("API para gestión de reservas, notificaciones y pagos en un sistema de booking. " +
                                "Incluye soporte para usuarios, servicios ofrecidos, validaciones y paginación.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Alejandro Carullo")
                                .email("alejandrojuliancarullo@gmail.com")
                                .url("https://www.linkedin.com/in/alejandro-carullo-java-dev-jr"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio en GitHub")
                        .url("https://github.com/AlePrimo/Booking-System-App"));



    }


    @Bean
    public OpenApiCustomizer customisePageable() {
        return openApi -> openApi.getComponents()
                .addParameters("page", new Parameter()
                        .name("page")
                        .in("query")
                        .description("Número de página (0..N)")
                        .required(false)
                        .schema(new IntegerSchema()._default(0)))
                .addParameters("size", new Parameter()
                        .name("size")
                        .in("query")
                        .description("Cantidad de elementos por página")
                        .required(false)
                        .schema(new IntegerSchema()._default(20)))
                .addParameters("sort", new Parameter()
                        .name("sort")
                        .in("query")
                        .description("Criterio de ordenación: propiedad,(asc|desc)")
                        .required(false)
                        .schema(new StringSchema()));
    }
}






