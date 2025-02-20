package com.kuit.moamoa.consumption.challenge.repository;

import com.kuit.moamoa.consumption.challenge.domain.ConsumptionChallenge;
import com.kuit.moamoa.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionChallengeRepository extends JpaRepository<ConsumptionChallenge, Long> {
    List<ConsumptionChallenge> findAllByUser(User user);

    List<ConsumptionChallenge> findAllByUserAndEndDateBeforeOrderByStartDateAsc(User user, LocalDate endDate);

    List<ConsumptionChallenge> findAllByUserAndEndDateBeforeOrderByStartDateDesc(User user, LocalDate endDate);

    List<ConsumptionChallenge> findAllByUserAndEndDateBeforeOrderByPrizeDesc(User user, LocalDate endDate);

    Optional<ConsumptionChallenge> findFirstByUserOrderByStartDateDesc(User user);

    Optional<ConsumptionChallenge> findByUserAndEndDateGreaterThanEqual(User user, LocalDate endDate);

    List<ConsumptionChallenge> findAllByUserAndEndDateLessThan(User user, LocalDate endDate);
}
