package com.example.kickoffbackend.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
