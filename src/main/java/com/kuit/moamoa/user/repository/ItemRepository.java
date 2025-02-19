package com.kuit.moamoa.user.repository;

import com.kuit.moamoa.user.domain.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Override
    @Cacheable(value = "allItems")
    List<Item> findAll();

    @Override
    @Cacheable(value = "item", key = "#id")
    Optional<Item> findById(Long id);
}
