package com.cmyk.ego.speaktoyouspring.api.hub.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEgoId(Long egoId);

    Optional<Notification> findByEgoIdAndPersonalityId(Long egoId, Long personalityId);

    void deleteAllByEgoId(Long egoId);
}
