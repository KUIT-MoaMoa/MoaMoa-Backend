package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Challenge;
import com.kuit.moamoa.domain.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    // 사용자의 진행중인 챌린지 조회
    @Query("SELECT c FROM Challenge c JOIN c.progressList p " +
            "WHERE p.user.id = :userId AND c.status = 'ONGOING'")
    List<Challenge> findOngoingChallengesByUserId(@Param("userId") Long userId);

    // 공개 챌린지 조회 (인기순)
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "ORDER BY SIZE(c.progressList) DESC")
    List<Challenge> findPublicChallengesByParticipantCountDesc();

    // 공개 챌린지 조회 (최신순)
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "ORDER BY c.createdAt DESC")
    List<Challenge> findPublicChallengesByCreatedAtDesc();

    // 공개 챌린지 조회 (모집마감 임박순)
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "ORDER BY c.recruitmentDeadline ASC")
    List<Challenge> findPublicChallengesByRecruitmentDeadlineAsc();

    // 공개 챌린지 조회 (코인순)
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "ORDER BY c.battleCoin DESC")
    List<Challenge> findPublicChallengesByBattleCoinDesc();

    // 친구 챌린지 조회
    @Query("SELECT DISTINCT c FROM Challenge c " +
            "JOIN c.progressList p " +
            "JOIN Friendship f " +
            "WHERE c.publicChallenge = false " +
            "AND c.status = 'RECRUITING' " +
            "AND ((f.fromUserId = :userId AND f.toUserId = p.user.id) " +
            "OR (f.toUserId = :userId AND f.fromUserId = p.user.id)) " +
            "AND f.status = 'ACTIVE'")
    List<Challenge> findFriendsChallenges(@Param("userId") Long userId);

    List<Challenge> findByStatusAndStartDateLessThanEqual(
            ChallengeStatus status,
            LocalDateTime dateTime
    );

    List<Challenge> findByStatusAndEndDateLessThanEqual(
            ChallengeStatus status,
            LocalDateTime dateTime
    );

    @Query("SELECT c FROM Challenge c JOIN c.progressList p " +
            "WHERE p.user.id = :userId " +
            "AND c.status = com.kuit.moamoa.domain.ChallengeStatus.COMPLETED " +
            "AND p.isGoalAchieved = true " +
            "AND p.rewardClaimed = false")
    List<Challenge> findUnclaimedCompletedChallengesByUserId(@Param("userId") Long userId);
}