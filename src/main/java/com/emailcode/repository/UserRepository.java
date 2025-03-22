package com.emailcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.emailcode.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}