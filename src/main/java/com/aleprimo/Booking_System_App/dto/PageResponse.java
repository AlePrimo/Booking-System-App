package com.aleprimo.Booking_System_App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta paginada genérica")
public class PageResponse<T> {

    @Schema(description = "Lista de elementos de la página")
    private List<T> content;

    @Schema(description = "Número de página actual (0-indexado)", example = "0")
    private int pageNumber;

    @Schema(description = "Tamaño de la página", example = "10")
    private int pageSize;

    @Schema(description = "Total de elementos", example = "100")
    private long totalElements;

    @Schema(description = "Total de páginas", example = "10")
    private int totalPages;

    @Schema(description = "Indica si es la última página", example = "false")
    private boolean last;
}
