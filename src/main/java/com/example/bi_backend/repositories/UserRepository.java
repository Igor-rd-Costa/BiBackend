package com.example.bi_backend.repositories;

import com.example.bi_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    UserDetails findByNormalizedEmail(String normalizedEmail);
}
