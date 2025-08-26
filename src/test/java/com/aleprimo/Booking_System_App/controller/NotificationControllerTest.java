package com.aleprimo.Booking_System_App.controller;


import com.aleprimo.Booking_System_App.controller.notification.NotificationController;
import com.aleprimo.Booking_System_App.dto.notification.NotificationRequestDTO;
import com.aleprimo.Booking_System_App.dto.notification.NotificationResponseDTO;
import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.mapper.notification.NotificationMapper;
import com.aleprimo.Booking_System_App.service.NotificationService;
import com.aleprimo.Booking_System_App.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private NotificationMapper notificationMapper;

    private User recipient;
    private Notification notification;
    private NotificationRequestDTO requestDTO;
    private NotificationResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        recipient = User.builder()
                .id(1L)
                .name("Juan")
                .email("juan@mail.com")
                .password("1234")
                .roles(Set.of(Role.CUSTOMER))
                .build();

        notification = Notification.builder()
                .id(1L)
                .type(NotificationType.EMAIL)
                .message("Test Notification")
                .recipient(recipient)
                .type(NotificationType.EMAIL)
                .sent(false)
                .build();

        requestDTO = NotificationRequestDTO.builder()
                .recipientId(1L)
                .message("Test Notification")
                .type(NotificationType.EMAIL)
                .build();

        responseDTO = NotificationResponseDTO.builder()
                .id(1L)
                .message("Test Notification")
                .recipientId(1L)
                .type(NotificationType.EMAIL)
                .sent(false)
                .build();
    }

    @Test
    void testCreateNotification() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(recipient));
        when(notificationMapper.toEntity(any(NotificationRequestDTO.class), any(User.class))).thenReturn(notification);
        when(notificationService.createNotification(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toDTO(any(Notification.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/notifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetNotificationById() throws Exception {
        when(notificationService.getNotificationById(1L)).thenReturn(Optional.of(notification));
        when(notificationMapper.toDTO(any(Notification.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipientId").value(1L));
    }

    @Test
    void testGetAllNotifications() throws Exception {
        when(notificationService.getAllNotifications(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(notification)));
        when(notificationMapper.toDTO(any(Notification.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/api/notifications?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recipientId").value(1L));
    }

    @Test
    void testDeleteNotification() throws Exception {
        mockMvc.perform(delete("/api/notifications/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
