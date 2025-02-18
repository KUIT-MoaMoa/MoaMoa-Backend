package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.ChallengeProgress;
import com.kuit.moamoa.domain.ChallengeStatus;
import com.kuit.moamoa.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeProgressRepository extends JpaRepository<ChallengeProgress, Long> {
    List<ChallengeProgress> findAllByUser(User user);

    // 특정 사용자가 참여중인, 특정 상태의 챌린지 ID 목록 조회
    @Query("SELECT cp.challenge.id FROM ChallengeProgress cp " +
            "WHERE cp.user.id = :userId AND cp.challenge.status IN :statuses AND cp.status = 'ACTIVE'")
    List<Long> findChallengeIdsByUserIdAndChallengeStatus(
            @Param("userId") Long userId,
            @Param("statuses") List<ChallengeStatus> statuses
    );

    // 특정 사용자가 특정 챌린지 목록 중 하나라도 참여하고 있는지 확인
    boolean existsByUserIdAndChallengeIdIn(Long userId, List<Long> challengeIds);
}
