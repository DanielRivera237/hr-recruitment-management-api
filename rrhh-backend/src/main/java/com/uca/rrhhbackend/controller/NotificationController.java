package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.NotificationRequest;
import com.uca.rrhhbackend.dto.response.NotificationResponse;
import com.uca.rrhhbackend.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(
            @Valid @RequestBody NotificationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.create(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> findByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                notificationService.findByUser(userId)
        );
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>> findUnreadByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                notificationService.findUnreadByUser(userId)
        );
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                notificationService.markAsRead(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}