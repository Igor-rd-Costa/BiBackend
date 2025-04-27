package com.example.bi_backend.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
    USER ("User"),
    ADMIN ("Admin");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
