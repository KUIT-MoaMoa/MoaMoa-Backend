package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Challenge;
import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    // 활성화된 UserGroup 조회
    Optional<UserGroup> findByIdAndStatus(Long id, Status status);

    // Challenge별 UserGroup 목록 조회
    List<UserGroup> findByChallengeAndStatus(Challenge challenge, Status status);

    // 특정 사용자가 참여중인 UserGroup 목록 조회
    @Query("SELECT ug FROM UserGroup ug " +
            "JOIN ug.userUserGroupJunctions uugj " +
            "WHERE uugj.user = :user AND ug.status = :status")
    List<UserGroup> findByUserAndStatus(@Param("user") User user, @Param("status") Status status);
}
