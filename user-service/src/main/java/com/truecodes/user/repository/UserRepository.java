package com.truecodes.user.repository;

import com.truecodes.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByContactNumber(String contactNumber);
}

