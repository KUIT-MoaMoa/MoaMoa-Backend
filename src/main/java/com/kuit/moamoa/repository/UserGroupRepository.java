package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findById(Long id);
    List<UserGroup> findAll();
}
