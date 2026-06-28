package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.NotificationRequest;
import com.uca.rrhhbackend.dto.response.NotificationResponse;
import com.uca.rrhhbackend.entity.Notification;

public final class NotificationMapper {

    private NotificationMapper() {
    }

    public static Notification toEntity(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.title().trim());
        notification.setMessage(request.message().trim());
        return notification;
    }

    public static NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getRead(),
                notification.getCreatedAt(),
                notification.getUser().getId()
        );
    }
}