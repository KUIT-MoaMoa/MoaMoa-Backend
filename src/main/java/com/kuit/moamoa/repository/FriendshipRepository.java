package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Friendship;
import com.kuit.moamoa.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.fromUserId = :userId OR f.toUserId = :userId) " +
            "AND f.status = 'ACTIVE'")
    List<Friendship> findAllFriendships(@Param("userId") Long userId);

    boolean existsByFromUserIdAndToUserIdAndStatus(Long fromUserId, Long toUserId, Status status);


}
