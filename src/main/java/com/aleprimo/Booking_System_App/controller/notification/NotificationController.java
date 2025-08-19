package com.aleprimo.Booking_System_App.controller.notification;


import com.aleprimo.Booking_System_App.dto.notification.NotificationRequestDTO;
import com.aleprimo.Booking_System_App.dto.notification.NotificationResponseDTO;
import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.mapper.notification.NotificationMapper;
import com.aleprimo.Booking_System_App.service.NotificationService;
import com.aleprimo.Booking_System_App.service.UserService;
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
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Endpoints para gestión de notificaciones")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    @PostMapping
    @Operation(summary = "Crear una notificación",
               description = "Crea una notificación para un usuario destinatario",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Notificación creada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Error de validación")
               })
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid @RequestBody NotificationRequestDTO dto) {
        User recipient = userService.getUserById(dto.getRecipientId()).orElseThrow();
        Notification notification = notificationService.createNotification(notificationMapper.toEntity(dto, recipient));
        return ResponseEntity.ok(notificationMapper.toDTO(notification));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una notificación por ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
                   @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
               })
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(notificationMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todas las notificaciones con paginación",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Lista de notificaciones")
               })
    public ResponseEntity<Page<NotificationResponseDTO>> getAllNotifications(Pageable pageable) {
        Page<NotificationResponseDTO> page = notificationService.getAllNotifications(pageable)
                .map(notificationMapper::toDTO);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una notificación",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Notificación eliminada"),
                   @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
               })
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
