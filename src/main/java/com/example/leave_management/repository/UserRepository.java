package com.example.leave_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.leave_management.entity.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsByUsername(String username);
}
