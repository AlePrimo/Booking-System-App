package com.aleprimo.Booking_System_App.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OpenApiConfig.class)
class OpenApiConfigTest {

    @Autowired
    private OpenApiConfig openApiConfig;

    private OpenAPI openAPI;

    @BeforeEach
    void setUp() {
        openAPI = new OpenAPI().components(new Components());
    }

    @Test
    void bookingSystemOpenAPI_shouldReturnConfiguredOpenAPI() {
        OpenAPI api = openApiConfig.bookingSystemOpenAPI();

        assertThat(api).isNotNull();
        Info info = api.getInfo();
        assertThat(info.getTitle()).isEqualTo("Booking System API");
        assertThat(info.getVersion()).isEqualTo("1.0.0");
        assertThat(info.getDescription()).contains("API para gestión de reservas");
        assertThat(info.getContact().getName()).isEqualTo("Alejandro Carullo");
        assertThat(info.getContact().getEmail()).isEqualTo("alejandrojuliancarullo@gmail.com");
        assertThat(info.getLicense().getName()).isEqualTo("Apache 2.0");
        assertThat(api.getExternalDocs().getDescription()).isEqualTo("Repositorio en GitHub");
    }

    @Test
    void customisePageable_shouldAddPageableParameters() {
        OpenApiCustomizer customizer = openApiConfig.customisePageable();
        customizer.customise(openAPI);

        Parameter pageParam = openAPI.getComponents().getParameters().get("page");
        Parameter sizeParam = openAPI.getComponents().getParameters().get("size");
        Parameter sortParam = openAPI.getComponents().getParameters().get("sort");

        assertThat(pageParam).isNotNull();
        assertThat(pageParam.getDescription()).contains("Número de página");
        assertThat(pageParam.getSchema().getDefault()).isEqualTo(0);

        assertThat(sizeParam).isNotNull();
        assertThat(sizeParam.getSchema().getDefault()).isEqualTo(20);

        assertThat(sortParam).isNotNull();
        assertThat(sortParam.getDescription()).contains("Criterio de ordenación");
    }
}
