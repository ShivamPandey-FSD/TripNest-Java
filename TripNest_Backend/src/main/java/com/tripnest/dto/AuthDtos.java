package com.tripnest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public record Register(@NotBlank String name, @NotBlank @Email String email,
                           @NotBlank @Size(min = 6) String password) {
    }

    public record Login(@NotBlank String email, @NotBlank String password) {
    }

    public record AuthResponse(String token, Long id, String name, String email, String role) {
    }
}
