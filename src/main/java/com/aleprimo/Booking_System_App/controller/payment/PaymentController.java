package com.aleprimo.Booking_System_App.controller.payment;


import com.aleprimo.Booking_System_App.dto.PageResponse;
import com.aleprimo.Booking_System_App.dto.payment.PaymentRequestDTO;
import com.aleprimo.Booking_System_App.dto.payment.PaymentResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import com.aleprimo.Booking_System_App.entity.enums.PaymentStatus;
import com.aleprimo.Booking_System_App.mapper.payment.PaymentMapper;
import com.aleprimo.Booking_System_App.service.BookingService;
import com.aleprimo.Booking_System_App.service.NotificationService;
import com.aleprimo.Booking_System_App.service.PaymentService;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Endpoints para gestión de pagos")
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final PaymentMapper paymentMapper;
    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Crear un pago y notificar al proveedor y al cliente",
            description = "Registra un pago asociado a una reserva, notifica al proveedor correspondiente y también al cliente que realizó el pago",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pago creado correctamente y notificaciones enviadas"),
                    @ApiResponse(responseCode = "400", description = "Error de validación")
            })
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO dto) {

        Booking booking = bookingService.getBookingById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        paymentService.getPaymentByBookingId(dto.getBookingId())
                .ifPresent(p -> {
                    throw new RuntimeException("Ya existe un pago registrado para esta reserva");
                });

        Payment payment = paymentMapper.toEntity(dto, booking);
        payment.setStatus(PaymentStatus.PAID);
        Payment savedPayment = paymentService.createPayment(payment);

        User provider = booking.getOffering().getProvider();
        User customer = booking.getCustomer();


        Notification notificationToProvider = Notification.builder()
                .type(NotificationType.EMAIL)
                .message("Has recibido un nuevo pago de " +
                        customer.getName() +
                        " por la reserva #" + booking.getId() +
                        " por un monto de $" + dto.getAmount())
                .recipient(provider)
                .sent(false)
                .build();

        notificationService.createNotification(notificationToProvider);


        Notification notificationToCustomer = Notification.builder()
                .type(NotificationType.EMAIL)
                .message("Has realizado un pago de $" + dto.getAmount() +
                        " por la reserva #" + booking.getId() +
                        " del servicio " + booking.getOffering().getDescription())
                .recipient(customer)
                .sent(false)
                .build();

        notificationService.createNotification(notificationToCustomer);

        return ResponseEntity.ok(paymentMapper.toDTO(savedPayment));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    @Operation(summary = "Listar todos los pagos con paginación")
    public ResponseEntity<PageResponse<PaymentResponseDTO>> getAllPayments(Pageable pageable) {
        Page<PaymentResponseDTO> page = paymentService.getAllPayments(pageable)
                .map(paymentMapper::toDTO);
        return ResponseEntity.ok(PageResponseUtil.from(page));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pago por ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Pago encontrado"),
                   @ApiResponse(responseCode = "404", description = "Pago no encontrado")
               })
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(paymentMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
