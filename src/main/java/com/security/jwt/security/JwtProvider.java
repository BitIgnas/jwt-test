package com.security.jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import static java.util.Date.from;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    private final Long JWT_EXPIRATION = 900000L;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/jwtStore.jks");
            keyStore.load(resourceAsStream, "Lietkabelis".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(JWT_EXPIRATION)))
                .compact();
    }

    public String generateTokenWithUserName(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(JWT_EXPIRATION)))
                .compact();
    }

    public PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("jwtStore", "Lietkabelis".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("jwtStore").getPublicKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }

    public Long getJwtExpiration() {
        return JWT_EXPIRATION;
    }
}
