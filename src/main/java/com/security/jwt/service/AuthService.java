package com.security.jwt.service;

import com.security.jwt.dto.AuthenticationResponse;
import com.security.jwt.dto.LoginRequest;
import com.security.jwt.dto.RefreshTokenRequest;
import com.security.jwt.dto.RegisterRequest;
import com.security.jwt.model.User;
import com.security.jwt.repository.UserRepository;
import com.security.jwt.repository.VerificationTokenRepository;
import com.security.jwt.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;


    public void register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = verificationTokenService.generateVerificationToken(user);
        verificationTokenService.sendVerificationEmail(user.getEmail(), token);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpiration()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpiration()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User userPrincipal = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }

    public void logout(RefreshTokenRequest refreshTokenRequest) {
        SecurityContextHolder.clearContext();
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }

}
