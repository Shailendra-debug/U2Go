package com.skushwaha.u2go.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "urls",
        indexes = {
                @Index(name = "idx_urls_short_code", columnList = "short_code"),
                @Index(name = "idx_urls_user_id", columnList = "user_id"),
                @Index(name = "idx_urls_expires_at", columnList = "expires_at"),
                @Index(name = "idx_urls_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    @Builder.Default
    private UUID id = UUID.randomUUID();


    /**
     * Original URL.
     */
    @Column(
            name = "original_url",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String originalUrl;


    /**
     * Generated short code.
     *
     * Example:
     * aB72xK
     */
    @Column(
            name = "short_code",
            nullable = false,
            unique = true,
            length = 20
    )
    private String shortCode;


    /**
     * Optional custom alias.
     *
     * Example:
     * my-video
     */
    @Column(
            name = "custom_alias",
            length = 50
    )
    private String customAlias;


    /**
     * Email of the user who created the URL.
     */
    @Column(
            name = "user_email",
            nullable = false,
            length = 255
    )
    private String userEmail;


    /**
     * Plan of the user when creating the URL.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "plan",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private UrlPlan plan = UrlPlan.FREE;


    /**
     * Number of clicks.
     */
    @Column(
            name = "click_count",
            nullable = false
    )
    @Builder.Default
    private Long clickCount = 0L;


    /**
     * Whether URL is active.
     */
    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;


    /**
     * Creation time.
     */
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;


    /**
     * Last update time.
     */
    @Column(
            name = "updated_at",
            nullable = false
    )
    private Instant updatedAt;


    /**
     * Expiration time.
     *
     * NULL = never expires.
     */
    @Column(name = "expires_at")
    private Instant expiresAt;


    @PrePersist
    protected void onCreate() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        Instant now = Instant.now();

        createdAt = now;
        updatedAt = now;

        if (clickCount == null) {
            clickCount = 0L;
        }

        if (active == null) {
            active = true;
        }

        if (plan == null) {
            plan = UrlPlan.FREE;
        }
    }


    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }


    public boolean isExpired() {
        return expiresAt != null
                && Instant.now().isAfter(expiresAt);
    }


    public boolean isAvailable() {
        return Boolean.TRUE.equals(active)
                && !isExpired();
    }


    public void incrementClickCount() {

        if (clickCount == null) {
            clickCount = 0L;
        }

        clickCount++;
    }
}
