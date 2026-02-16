package com.mediview.domain.notification.service;

import com.mediview.domain.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getNotifications(String email);

    NotificationResponse markAsRead(Long id);
}
