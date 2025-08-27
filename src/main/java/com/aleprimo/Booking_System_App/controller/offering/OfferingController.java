package com.aleprimo.Booking_System_App.controller.offering;


import com.aleprimo.Booking_System_App.dto.PageResponse;
import com.aleprimo.Booking_System_App.dto.offering.OfferingRequestDTO;
import com.aleprimo.Booking_System_App.dto.offering.OfferingResponseDTO;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.exception.ResourceNotFoundException;
import com.aleprimo.Booking_System_App.mapper.offering.OfferingMapper;
import com.aleprimo.Booking_System_App.service.OfferingService;
import com.aleprimo.Booking_System_App.service.UserService;
import com.aleprimo.Booking_System_App.util.PageResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offerings")
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "Endpoints para gestión de servicios/ofertas")
public class OfferingController {

    private final OfferingService offeringService;
    private final OfferingMapper offeringMapper;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Crear un nuevo servicio",
               description = "Crea un servicio con nombre, descripción, duración y precio",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Servicio creado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Error de validación")
               })
    public ResponseEntity<OfferingResponseDTO> createOffering(@Valid @RequestBody OfferingRequestDTO dto) {
        User provider = userService.getUserById(dto.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID " + dto.getProviderId()));

        Offering offering = offeringService.createOffering(offeringMapper.toEntity(dto, provider));
        return ResponseEntity.ok(offeringMapper.toDTO(offering));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un servicio",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Servicio actualizado"),
                   @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
               })
    public ResponseEntity<OfferingResponseDTO> updateOffering(@PathVariable Long id,
                                                              @Valid @RequestBody OfferingRequestDTO dto) {
        User provider = userService.getUserById(dto.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID " + dto.getProviderId()));

        Offering offering = offeringService.updateOffering(id, offeringMapper.toEntity(dto, provider));
        return ResponseEntity.ok(offeringMapper.toDTO(offering));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un servicio",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Servicio eliminado"),
                   @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
               })
    public ResponseEntity<Void> deleteOffering(@PathVariable Long id) {
        offeringService.deleteOffering(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un servicio por ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Servicio encontrado"),
                   @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
               })
    public ResponseEntity<OfferingResponseDTO> getOfferingById(@PathVariable Long id) {
        return offeringService.getOfferingById(id)
                .map(offeringMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todos los servicios con paginación",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Lista de servicios")
               })
    public ResponseEntity<PageResponse<OfferingResponseDTO>> getAllOfferings(Pageable pageable) {
        Page<OfferingResponseDTO> page = offeringService.getAllOfferings(pageable)
                .map(offeringMapper::toDTO);
        return ResponseEntity.ok(PageResponseUtil.from(page));
    }
}
