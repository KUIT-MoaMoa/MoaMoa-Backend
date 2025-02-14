package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.ConsumptionChallenge;
import com.kuit.moamoa.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionChallengeRepository extends JpaRepository<ConsumptionChallenge, Long> {
    List<ConsumptionChallenge> findAllByUser(User user);

    List<ConsumptionChallenge> findAllByUserOrderByStartDateAsc(User user);

    List<ConsumptionChallenge> findAllByUserOrderByStartDateDesc(User user);

    List<ConsumptionChallenge> findAllByUserOrderByPrizeDesc(User user);

    Optional<ConsumptionChallenge> findFirstByUserOrderByStartDateDesc(User user);
}
