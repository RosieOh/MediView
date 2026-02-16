package com.mediview.domain.notification.controller;

import com.mediview.domain.notification.dto.NotificationResponse;
import com.mediview.domain.notification.service.NotificationService;
import com.mediview.domain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Notification APIs")
@SecurityRequirement(name = "BearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get my notifications")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> list(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<NotificationResponse> result = notificationService.getNotifications(userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.<List<NotificationResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Notifications retrieved")
                        .data(result)
                        .build()
        );
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Long id) {
        NotificationResponse result = notificationService.markAsRead(id);
        return ResponseEntity.ok(
                ApiResponse.<NotificationResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Notification marked as read")
                        .data(result)
                        .build()
        );
    }
}
