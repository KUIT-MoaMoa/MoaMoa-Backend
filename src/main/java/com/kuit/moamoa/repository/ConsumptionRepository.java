package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
}
