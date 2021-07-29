package com.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;
    @Email
    @NotNull
    @NotBlank
    private String email;
}
