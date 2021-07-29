package com.security.jwt.controller;

import com.security.jwt.dto.AuthenticationResponse;
import com.security.jwt.dto.LoginRequest;
import com.security.jwt.dto.RefreshTokenRequest;
import com.security.jwt.dto.RegisterRequest;
import com.security.jwt.model.User;
import com.security.jwt.service.AuthService;
import com.security.jwt.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/verify/{token}")
    public ResponseEntity<String> verifyUserAccount(@PathVariable("token") String token) {
        verificationTokenService.verifyUserAccount(token);
        return new ResponseEntity<>("User Activated", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/login").toUriString());
        return ResponseEntity.created(uri).body(authService.login(loginRequest));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/token/refresh").toUriString());
        return ResponseEntity.created(uri).body(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        authService.logout(refreshTokenRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
