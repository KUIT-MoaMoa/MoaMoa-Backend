package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Notification;
import com.kuit.moamoa.domain.NotificationType;
import com.kuit.moamoa.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Status status);
    Optional<Notification> findByTypeAndRelationIdAndUserId(NotificationType type, Long relationId, Long userId);
}