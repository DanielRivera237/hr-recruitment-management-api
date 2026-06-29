package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.NotificationRequest;
import com.uca.rrhhbackend.dto.response.NotificationResponse;
import com.uca.rrhhbackend.entity.User;

import java.util.List;

public interface NotificationService {

    NotificationResponse create(NotificationRequest request);

    List<NotificationResponse> findMyNotifications(User currentUser);

    List<NotificationResponse> findMyUnreadNotifications(
            User currentUser
    );

    NotificationResponse markMyNotificationAsRead(
            User currentUser,
            Long id
    );

    void delete(Long id);
}