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
    public NotificationResponse create(NotificationRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        Notification notification =
                NotificationMapper.toEntity(request);

        notification.setUser(user);
        notification.setRead(false);

        return NotificationMapper.toResponse(
                notificationRepository.save(notification)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> findByUser(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> findUnreadByUser(Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId)
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        Notification notification = findEntity(id);

        if (Boolean.TRUE.equals(notification.getRead())) {
            throw new BusinessException(
                    "La notificación ya fue marcada como leída"
            );
        }

        notification.setRead(true);

        return NotificationMapper.toResponse(
                notificationRepository.save(notification)
        );
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
}