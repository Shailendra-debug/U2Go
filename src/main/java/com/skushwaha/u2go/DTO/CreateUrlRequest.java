package com.skushwaha.u2go.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUrlRequest(

        @NotBlank(message = "Original URL is required")
        @Size(max = 4096, message = "URL must not exceed 4096 characters")
        String originalUrl,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        @Size(max = 255, message = "Email must not exceed 255 characters")
        String userEmail,

        @Size(
                min = 3,
                max = 50,
                message = "Custom alias must be between 3 and 50 characters"
        )
        String customAlias
) {
}
