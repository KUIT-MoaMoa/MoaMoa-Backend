package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.ChallengeProgress;
import com.kuit.moamoa.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeProgressRepository extends JpaRepository<ChallengeProgress, Long> {
    List<ChallengeProgress> findAllByUser(User user);

}
