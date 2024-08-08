package com.example.kickoffbackend.user;


import com.example.kickoffbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByEmail(String email);
}
