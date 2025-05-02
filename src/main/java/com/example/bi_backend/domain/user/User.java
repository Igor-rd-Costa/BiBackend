package com.example.bi_backend.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    private String email;

    @Email
    @JsonIgnore
    @Column(name = "`normalizedEmail`")
    private String normalizedEmail;
    private String username;
    @Column(name = "`firstName`")
    private String firstName;
    @Column(name = "`lastName`")
    private String lastName;
    @JsonIgnore
    private String password;
    private UserRole role = UserRole.USER;
    private boolean online = false;
    @Column(name = "`emailVerified`")
    private boolean emailVerified = false;

    public User(String email, String password) {
        this.email = email;
        this.normalizedEmail = email.toUpperCase();
        this.username = email.substring(0, email.indexOf('@'));
        this.firstName = null;
        this.lastName = null;
        this.password = password;
        this.role = UserRole.USER;
        this.online = false;
        this.emailVerified = false;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var roles = new ArrayList<SimpleGrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (this.role == UserRole.ADMIN) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
