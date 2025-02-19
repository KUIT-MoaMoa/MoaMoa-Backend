package com.kuit.moamoa.consumption.record.repository;

import com.kuit.moamoa.consumption.record.domain.Consumption;
import com.kuit.moamoa.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    List<Consumption> findAllByUserAndCreatedAtAfter(User user, LocalDateTime localDateTime);
}
