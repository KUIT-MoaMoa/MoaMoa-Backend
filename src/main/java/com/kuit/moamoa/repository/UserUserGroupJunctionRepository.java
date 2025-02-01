package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.domain.UserGroup;
import com.kuit.moamoa.domain.UserUserGroupJunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserUserGroupJunctionRepository extends JpaRepository<UserUserGroupJunction, Long> {
    // UserGroup의 참여자 목록 조회
    List<UserUserGroupJunction> findByUserGroupAndStatus(UserGroup userGroup, Status status);

    // 특정 사용자의 UserGroup 참여 여부 확인
    boolean existsByUserAndUserGroupAndStatus(User user, UserGroup userGroup, Status status);

    // 특정 UserGroup의 참여자 수 조회
    @Query("SELECT COUNT(uugj) FROM UserUserGroupJunction uugj " +
            "WHERE uugj.userGroup = :userGroup AND uugj.status = :status")
    Long countByUserGroupAndStatus(@Param("userGroup") UserGroup userGroup,
                                   @Param("status") Status status);
}
