package com.ssafy.edith.user.repository;

import com.ssafy.edith.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String userEmail);
}
