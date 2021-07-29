package com.security.jwt.service;

import com.security.jwt.model.User;
import com.security.jwt.model.VerificationToken;
import com.security.jwt.repository.UserRepository;
import com.security.jwt.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyUserAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);
        findUserAndEnable(verificationToken.orElseThrow(() -> new RuntimeException("Invalid token")));
    }

    public void findUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No user was found"));
        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);
    }

    public void sendVerificationEmail(String email, String token) {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        User user = userOptional
                .orElseThrow(() -> new RuntimeException("Invalid user"));

        if(!user.isEnabled()) {
            mailService.sendMail(email, token);
        }
    }
}
