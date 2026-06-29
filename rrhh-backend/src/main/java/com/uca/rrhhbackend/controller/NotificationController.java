package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.NotificationRequest;
import com.uca.rrhhbackend.dto.response.NotificationResponse;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.service.CurrentUserService;
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
    private final CurrentUserService currentUserService;

    public NotificationController(
            NotificationService notificationService,
            CurrentUserService currentUserService
    ) {
        this.notificationService = notificationService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(
            @Valid @RequestBody NotificationRequest request
    ) {
        NotificationResponse response =
                notificationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<NotificationResponse>>
    findMyNotifications() {

        User currentUser =
                currentUserService.getCurrentUser();

        return ResponseEntity.ok(
                notificationService.findMyNotifications(
                        currentUser
                )
        );
    }

    @GetMapping("/me/unread")
    public ResponseEntity<List<NotificationResponse>>
    findMyUnreadNotifications() {

        User currentUser =
                currentUserService.getCurrentUser();

        return ResponseEntity.ok(
                notificationService
                        .findMyUnreadNotifications(currentUser)
        );
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse>
    markMyNotificationAsRead(
            @PathVariable Long id
    ) {
        User currentUser =
                currentUserService.getCurrentUser();

        return ResponseEntity.ok(
                notificationService
                        .markMyNotificationAsRead(
                                currentUser,
                                id
                        )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        notificationService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}