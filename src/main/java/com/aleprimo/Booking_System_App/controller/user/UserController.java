package com.aleprimo.Booking_System_App.controller.user;


import com.aleprimo.Booking_System_App.dto.PageResponse;
import com.aleprimo.Booking_System_App.dto.user.UserRequestDTO;
import com.aleprimo.Booking_System_App.dto.user.UserResponseDTO;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.mapper.user.UserMapper;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Crear un nuevo usuario",
            description = "Crea un usuario con nombre, email, contraseña y roles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario creado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación")
            })
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        User user = userService.createUser(userMapper.toEntity(dto));
        return ResponseEntity.status(201).body(userMapper.toDTO(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario existente",
            description = "Actualiza los datos de un usuario por su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            })
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UserRequestDTO dto) {
        User user = userService.updateUser(id, userMapper.toEntity(dto));
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario",
            description = "Elimina un usuario por su ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            })
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(userMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")

    @GetMapping
    @Operation(summary = "Listar todos los usuarios con paginación",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios")
            })
    public ResponseEntity<PageResponse<UserResponseDTO>> getAllUsers(Pageable pageable) {
        Page<UserResponseDTO> page = userService.getAllUsers(pageable)
                .map(userMapper::toDTO);
        return ResponseEntity.ok(PageResponseUtil.from(page));
    }
}
