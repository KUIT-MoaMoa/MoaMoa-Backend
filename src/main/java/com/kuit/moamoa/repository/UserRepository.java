package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // login_id로 사용자 조회 (중복 방지를 위해 Optional)
    Optional<User> findByLoginId(String loginId);

    // nickname으로 사용자 조회
    Optional<User> findByNickname(String nickname);

    // 존재 여부 확인
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
}
