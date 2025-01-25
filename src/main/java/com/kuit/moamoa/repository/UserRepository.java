package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

//    public AuthRepository(EntityManager em) {this.em = em;}

    public void save(User user) { em.persist(user); }

    public Boolean existByUserId(String id) {
        User founUser = em.find(User.class, id);
        return founUser != null;
    }

    public Boolean existByNickname(String nickname) {
        User founUser = em.find(User.class, nickname);
        return founUser != null;
    }
}
