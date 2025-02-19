package com.kuit.moamoa.user.repository;

import com.kuit.moamoa.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // existByNickname -> existsByNickname으로 수정
    boolean existsByNickname(String nickname);

    // Nickname으로 사용자 찾기 (CustomOAuth2UserService와의 호환성을 위해 Optional 사용하지 않음)
    User findByNickname(String nickname);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    List<User> findByNicknameContaining(String nickname);
}