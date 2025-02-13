package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Challenge;
import com.kuit.moamoa.domain.ChallengeCategory;
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
            "AND SIZE(c.progressList) < c.headCount " +  // 인원수 체크 조건 추가
            "AND NOT EXISTS (SELECT p FROM ChallengeProgress p WHERE p.challenge = c AND p.user.id = :userId) " +
            "ORDER BY SIZE(c.progressList) DESC")
    List<Challenge> findPublicChallengesByParticipantCountDesc(@Param("userId") Long userId);

    // 공개 챌린지 조회 (최신순)
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "AND SIZE(c.progressList) < c.headCount " +  // 인원수 체크 조건 추가
            "AND NOT EXISTS (SELECT p FROM ChallengeProgress p WHERE p.challenge = c AND p.user.id = :userId) " +
            "ORDER BY c.createdAt DESC")
    List<Challenge> findPublicChallengesByCreatedAtDesc(@Param("userId") Long userId);

    // 공개 챌린지 조회 (모집마감 임박순)
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "AND SIZE(c.progressList) < c.headCount " +  // 인원수 체크 조건 추가
            "AND NOT EXISTS (SELECT p FROM ChallengeProgress p WHERE p.challenge = c AND p.user.id = :userId) " +
            "ORDER BY c.recruitmentDeadline ASC")
    List<Challenge> findPublicChallengesByRecruitmentDeadlineAsc(@Param("userId") Long userId);

    // 공개 챌린지 조회 (코인순)
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "AND SIZE(c.progressList) < c.headCount " +  // 인원수 체크 조건 추가
            "AND NOT EXISTS (SELECT p FROM ChallengeProgress p WHERE p.challenge = c AND p.user.id = :userId) " +
            "ORDER BY c.battleCoin DESC")
    List<Challenge> findPublicChallengesByBattleCoinDesc(@Param("userId") Long userId);

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

    // 해당 유저의 아직 보상을 수령받지 않은 완료된 챌린지 확인
    @Query("SELECT c FROM Challenge c JOIN c.progressList p " +
            "WHERE p.user.id = :userId " +
            "AND c.status = com.kuit.moamoa.domain.ChallengeStatus.COMPLETED " +
            "AND p.isGoalAchieved = true " +
            "AND p.rewardClaimed = false")
    List<Challenge> findUnclaimedCompletedChallengesByUserId(@Param("userId") Long userId);

    // 해당 유저의 ChallengeProgress 확인
    @Query("SELECT p FROM ChallengeProgress p WHERE p.challenge.id = :challengeId AND p.user.id = :userId")
    Optional<ChallengeProgress> findProgressByChallengeIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    // 카테고리로 챌린지 검색
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "AND c.challengeCategory = :category " +
            "AND SIZE(c.progressList) < c.headCount " +
            "AND NOT EXISTS (SELECT p FROM ChallengeProgress p WHERE p.challenge = c AND p.user.id = :userId)")
    List<Challenge> findPublicChallengesByCategory(
            @Param("category") ChallengeCategory category,
            @Param("userId") Long userId
    );

    // 검색어로 챌린지 검색
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.publicChallenge = true " +
            "AND c.status = 'RECRUITING' " +
            "AND (LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND SIZE(c.progressList) < c.headCount " +
            "AND NOT EXISTS (SELECT p FROM ChallengeProgress p WHERE p.challenge = c AND p.user.id = :userId)")
    List<Challenge> findPublicChallengesByKeyword(
            @Param("keyword") String keyword,
            @Param("userId") Long userId
    );

    // UserGroup의 챌린지 기록 조회
    @Query("SELECT DISTINCT c FROM Challenge c " +
            "LEFT JOIN FETCH c.progressList p " +
            "WHERE c.userGroup.id = :groupId " +
            "AND c.status = 'COMPLETED' " +
            "ORDER BY c.endDate DESC")
    List<Challenge> findCompletedChallengesByGroupId(@Param("groupId") Long groupId);
}