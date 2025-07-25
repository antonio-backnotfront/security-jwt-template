package com.example.securityjwttemplate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) String login,
        @NotBlank @Size(min = 8) @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$") String password
) {
}
