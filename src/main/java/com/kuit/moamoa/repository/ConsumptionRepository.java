package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Consumption;
import com.kuit.moamoa.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    List<Consumption> findAllByUserAndCreatedAtAfter(User user, LocalDateTime localDateTime);
}
