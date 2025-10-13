package com.aleprimo.Booking_System_App.controller.booking;


import com.aleprimo.Booking_System_App.dto.PageResponse;
import com.aleprimo.Booking_System_App.dto.booking.BookingRequestDTO;
import com.aleprimo.Booking_System_App.dto.booking.BookingResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import com.aleprimo.Booking_System_App.mapper.booking.BookingMapper;
import com.aleprimo.Booking_System_App.service.BookingService;
import com.aleprimo.Booking_System_App.service.NotificationService;
import com.aleprimo.Booking_System_App.service.OfferingService;
import com.aleprimo.Booking_System_App.service.UserService;
import com.aleprimo.Booking_System_App.util.PageResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Endpoints para gestión de reservas")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final OfferingService offeringService;
    private final BookingMapper bookingMapper;
   private final NotificationService notificationService;

    @PostMapping
    @Operation(
            summary = "Crear una reserva",
            description = "Crea una reserva asignando cliente, servicio y fecha/hora",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reserva creada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación")
            })
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO dto) {
        User customer = userService.getUserById(dto.getCustomerId()).orElseThrow();
        Offering offering = offeringService.getOfferingById(dto.getOfferingId()).orElseThrow();

        Booking booking = bookingService.createBooking(
                bookingMapper.toEntity(dto, customer, offering)
        );

     

        return ResponseEntity.ok(bookingMapper.toDTO(booking));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reserva",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Reserva actualizada"),
                   @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
               })
    public ResponseEntity<BookingResponseDTO> updateBooking(@PathVariable Long id,
                                                            @Valid @RequestBody BookingRequestDTO dto) {
        User customer = userService.getUserById(dto.getCustomerId()).orElseThrow();
        Offering offering = offeringService.getOfferingById(dto.getOfferingId()).orElseThrow();

        Booking booking = bookingService.updateBooking(id, bookingMapper.toEntity(dto, customer, offering));
        return ResponseEntity.ok(bookingMapper.toDTO(booking));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Reserva eliminada"),
                   @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
               })
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar el status de  una reserva",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reserva actualizada"),
                    @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
            })
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(@PathVariable Long id,
                                                                  @RequestParam BookingStatus status) {
        Booking updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(bookingMapper.toDTO(updated));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reserva por ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
                   @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
               })
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(bookingMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas con paginación",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Lista de reservas")
               })
    public ResponseEntity<PageResponse<BookingResponseDTO>> getAllBookings(Pageable pageable) {
        Page<BookingResponseDTO> page = bookingService.getAllBookings(pageable)
                .map(bookingMapper::toDTO);
        return ResponseEntity.ok(PageResponseUtil.from(page));


    }
    @GetMapping("/customer/{customerId}")
    @Operation(
            summary = "Obtener reservas de un cliente",
            description = "Devuelve la lista paginada de reservas asociadas a un cliente por su ID"
    )
    public ResponseEntity<Page<BookingResponseDTO>> getBookingsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookings = bookingService.getBookingsByCustomerId(customerId, pageable);

        Page<BookingResponseDTO> dtoPage = bookings.map(booking -> BookingResponseDTO.builder()
                .id(booking.getId())
                .customerId(booking.getCustomer().getId())
                .offeringId(booking.getOffering().getId())
                .bookingDateTime(booking.getBookingDateTime())
                .status(booking.getStatus())
                .build()
        );

        return ResponseEntity.ok(dtoPage);
    }



}
