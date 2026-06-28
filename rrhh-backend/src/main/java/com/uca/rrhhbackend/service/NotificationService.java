package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.NotificationRequest;
import com.uca.rrhhbackend.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationResponse create(NotificationRequest request);

    List<NotificationResponse> findByUser(Long userId);

    List<NotificationResponse> findUnreadByUser(Long userId);

    NotificationResponse markAsRead(Long id);

    void delete(Long id);
}