package com.skushwaha.u2go.DTO;

import java.time.Instant;

public record PaidUrlResponse(
        String shortCode,
        String shortUrl,
        String originalUrl,
        Instant expiresAt,
        String email
) implements UrlResponseCreat {
}