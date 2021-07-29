package com.security.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private Instant expiresAt;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
}
