package com.skushwaha.u2go.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    @Builder.Default
    private UUID id = UUID.randomUUID();


    /**
     * User's name.
     */
    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;


    /**
     * Unique login email.
     */
    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 255
    )
    private String email;


    /**
     * BCrypt encoded password.
     */
    @Column(
            name = "password",
            nullable = false,
            length = 255
    )
    private String password;


    /**
     * Account role.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private UserRole role = UserRole.USER;


    /**
     * Whether account is enabled.
     */
    @Column(
            name = "enabled",
            nullable = false
    )
    @Builder.Default
    private Boolean enabled = true;


    /**
     * Whether email has been verified.
     */
    @Column(
            name = "email_verified",
            nullable = false
    )
    @Builder.Default
    private Boolean emailVerified = false;


    /**
     * Account creation time.
     */
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;


    /**
     * Last account update time.
     */
    @Column(
            name = "updated_at",
            nullable = false
    )
    private Instant updatedAt;


    /**
     * Last successful login.
     */
    @Column(name = "last_login_at")
    private Instant lastLoginAt;


    @PrePersist
    protected void onCreate() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        Instant now = Instant.now();

        createdAt = now;
        updatedAt = now;

        if (role == null) {
            role = UserRole.USER;
        }

        if (enabled == null) {
            enabled = true;
        }

        if (emailVerified == null) {
            emailVerified = false;
        }
    }


    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
