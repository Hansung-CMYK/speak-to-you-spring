package com.cmyk.ego.speaktoyouspring.api.hub.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByNotificationId(Long notificationId);
    Optional<Notification> findByNotificationIdAndIsDeletedIsFalse(Long notificationId);
    Long countByUidAndIsDeletedIsFalseAndIsReadFalse(String userId);
    Page<Notification> findByUidAndIsDeletedIsFalse(String userId, Pageable pageable);
}
