package com.cmyk.ego.speaktoyouspring.api.hub.notification;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.NotificationErrorCode;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserAccountRepository userAccountRepository;

    /// 사용자의 알림을 생성하는 API
    public Notification create(NotificationDTO notificationDTO) {
        return notificationRepository.save(notificationDTO.toEntity());
    }

    /// notification_id로 알림 조회 및 업데이트
    public Notification update(NotificationDTO notificationDTO) {
        Notification notification = notificationRepository.findByNotificationId(notificationDTO.getNotificationId()).orElseGet(notificationDTO::toEntity);
        return notificationRepository.save(notification);
    }

    /// notification_id로 알림 삭제
    public Notification delete(Long notificationId) {
        Notification notification = notificationRepository.findByNotificationIdAndIsDeletedIsFalse(notificationId).orElseThrow(
                () -> new ControlledException(NotificationErrorCode.ERROR_NOTIFICATION_NOT_FOUND)
        );
        notification.setIsDeleted(true);
        return notificationRepository.save(notification);
    }

    /// notification_id로 알림 읽기
    public Notification read(Long notificationId) {
        Notification notification = notificationRepository.findByNotificationIdAndIsDeletedIsFalse(notificationId).orElseThrow(
                () -> new ControlledException(NotificationErrorCode.ERROR_NOTIFICATION_NOT_FOUND)
        );
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    /// userId로 알림 개수 조회
    public Long unreadCount(String userId) {
        return notificationRepository.countByUidAndIsDeletedIsFalseAndIsReadFalse(userId);
    }

    /// 사용자 id로 알림 목록 조회
    public List<Notification> findNotificationListByUid(String userId, int pageNum, int pageSize) {
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(userId).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("createdAt")));
        return notificationRepository.findByUidAndIsDeletedIsFalse(userId, pageable).getContent();
    }
}
