package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.NotificationRequest;
import com.uca.rrhhbackend.dto.response.NotificationResponse;
import com.uca.rrhhbackend.entity.Notification;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.NotificationMapper;
import com.uca.rrhhbackend.repository.NotificationRepository;
import com.uca.rrhhbackend.repository.UserRepository;
import com.uca.rrhhbackend.service.NotificationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public NotificationResponse create(
            NotificationRequest request
    ) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        Notification notification =
                NotificationMapper.toEntity(request);

        notification.setUser(user);
        notification.setRead(false);

        Notification savedNotification =
                notificationRepository.save(notification);

        return NotificationMapper.toResponse(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> findMyNotifications(
            User currentUser
    ) {
        return notificationRepository
                .findByUserId(currentUser.getId())
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> findMyUnreadNotifications(
            User currentUser
    ) {
        return notificationRepository
                .findByUserIdAndReadFalse(currentUser.getId())
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }

    @Override
    public NotificationResponse markMyNotificationAsRead(
            User currentUser,
            Long id
    ) {
        Notification notification = findEntity(id);

        validateOwnership(notification, currentUser);

        if (Boolean.TRUE.equals(notification.getRead())) {
            throw new BusinessException(
                    "La notificación ya fue marcada como leída"
            );
        }

        notification.setRead(true);

        Notification savedNotification =
                notificationRepository.save(notification);

        return NotificationMapper.toResponse(savedNotification);
    }

    @Override
    public void delete(Long id) {
        Notification notification = findEntity(id);
        notificationRepository.delete(notification);
    }

    private Notification findEntity(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notificación no encontrada con id " + id
                        )
                );
    }

    private void validateOwnership(
            Notification notification,
            User currentUser
    ) {
        if (!notification.getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new BusinessException(
                    "No tienes permiso para modificar esta notificación"
            );
        }
    }
}