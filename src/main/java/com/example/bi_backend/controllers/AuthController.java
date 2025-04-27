package com.example.bi_backend.controllers;

import com.example.bi_backend.domain.user.AuthenticationDTO;
import com.example.bi_backend.domain.user.LoginResponseDTO;
import com.example.bi_backend.domain.user.User;
import com.example.bi_backend.domain.user.UserRole;
import com.example.bi_backend.infra.security.TokenService;
import com.example.bi_backend.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authManager.authenticate(usernamePassword);

            var token  = tokenService.generateToken((User)auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (BadCredentialsException|UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid AuthenticationDTO data) {
        try {
            if (this.repository.findByNormalizedEmail(data.email().toUpperCase()) != null) {
                return ResponseEntity.badRequest().build();
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.email(), encryptedPassword);
            this.repository.save(newUser);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
