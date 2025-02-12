package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.ConsumptionChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionChallengeRepository extends JpaRepository<ConsumptionChallenge, Long> {
}
