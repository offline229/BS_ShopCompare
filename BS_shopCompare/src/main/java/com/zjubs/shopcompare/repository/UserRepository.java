package com.zjubs.shopcompare.repository;

import com.zjubs.shopcompare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // 根据用户名查找用户
    Optional<User> findByUsername(String username);


}
