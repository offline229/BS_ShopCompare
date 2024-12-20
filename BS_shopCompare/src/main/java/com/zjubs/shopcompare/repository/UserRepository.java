package com.zjubs.shopcompare.repository;

import com.zjubs.shopcompare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 根据邮箱查找用户
    User findByEmail(String email);

}
