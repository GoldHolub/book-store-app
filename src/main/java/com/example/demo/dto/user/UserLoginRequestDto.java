package com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        @Size(min = 8, max = 50)
        String email,
        @NotBlank @Size(min = 8, max = 50)
        String password
) {
}
