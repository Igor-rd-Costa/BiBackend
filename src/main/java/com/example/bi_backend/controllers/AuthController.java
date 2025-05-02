package com.example.bi_backend.controllers;

import com.example.bi_backend.domain.user.AuthenticationDTO;
import com.example.bi_backend.domain.user.LoginResponseDTO;
import com.example.bi_backend.domain.user.User;
import com.example.bi_backend.infra.security.TokenService;
import com.example.bi_backend.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data, HttpServletResponse response) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authManager.authenticate(usernamePassword);

            var user = (User)auth.getPrincipal();
            var token  = tokenService.generateToken(user);

            response.addCookie(genAuthCookie(token));
            return ResponseEntity.ok(new LoginResponseDTO(user));
        } catch (BadCredentialsException|UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        response.addCookie(genDeleteCookie());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid AuthenticationDTO data, HttpServletResponse response) {
        try {
            if (this.repository.findByNormalizedEmail(data.email().toUpperCase()) != null) {
                return ResponseEntity.badRequest().build();
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User user = new User(data.email(), encryptedPassword);
            this.repository.save(user);

            var token = this.tokenService.generateToken(user);

            response.addCookie(genAuthCookie(token));
            return ResponseEntity.ok(new LoginResponseDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Cookie genAuthCookie(String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Lax");
        return cookie;
    }

    private Cookie genDeleteCookie() {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Lax");
        return cookie;
    }
}
