package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Challenge;
import com.kuit.moamoa.domain.ChallengeProgress;
import com.kuit.moamoa.domain.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    // 사용자의 참여중인 챌린지 조회
    @Query("SELECT c FROM Challenge c JOIN c.progressList p " +
            "WHERE p.user.id = :userId " +
            "AND (c.status = 'RECRUITING' OR c.status = 'ONGOING') " +
            "AND c.publicChallenge = true")
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

    @Query("SELECT DISTINCT c FROM Challenge c " +
            "JOIN c.progressList p1 ON p1.user.id = :userId " + // 로그인한 사용자가 참여한 챌린지
            "JOIN c.progressList p2 " + // 친구도 참여 중이어야 함
            "JOIN Friendship f ON (f.fromUserId = p2.user.id OR f.toUserId = p2.user.id) " +
            "WHERE c.publicChallenge = false " +
            "AND (c.status = 'RECRUITING' OR c.status = 'ONGOING')" +
            "AND (f.fromUserId = :userId OR f.toUserId = :userId) " + // 로그인한 사용자와 친구 관계
            "AND f.status = 'ACTIVE' " +
            "AND p2.user.id <> :userId") // 친구만 추가로 참여한 경우 필터링
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


    @Query("SELECT p FROM ChallengeProgress p WHERE p.challenge.id = :challengeId AND p.user.id = :userId")
    Optional<ChallengeProgress> findProgressByChallengeIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);
}