package com.skushwaha.u2go.DTO;

import com.skushwaha.u2go.Entity.UrlPlan;

import java.time.Instant;
import java.util.UUID;

public record UrlResponse(

        UUID id,

        String originalUrl,

        String shortCode,

        String shortUrl,

        String customAlias,

        String userEmail,

        UrlPlan plan,

        Long clickCount,

        Boolean active,

        Boolean isQr,

        Instant createdAt,

        Instant updatedAt,

        Instant expiresAt
) {
}
