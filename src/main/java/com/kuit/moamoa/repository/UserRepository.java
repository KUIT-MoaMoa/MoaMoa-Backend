package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.User;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository {

    private final EntityManager em;

    public void save(User user) { em.persist(user); }

//    public Boolean existByUserId(String id) {
//        User founUser = em.find(User.class, id);
//        return founUser != null;
//    }

    public Boolean existByNickname(String nickname) {
        List<User> users = em.createQuery("select u from User u where u.nickname = :nickname", User.class)
                        .setParameter("nickname", nickname)
                        .getResultList();
        return !users.isEmpty();
    }

    public User findByNickname(String nickname) {
        List<User> results = em.createQuery("select u from User u where u.nickname = :nickname", User.class)
                .setParameter("nickname", nickname)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}

