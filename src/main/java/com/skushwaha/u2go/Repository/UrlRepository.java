package com.skushwaha.u2go.Repository;

import com.skushwaha.u2go.Entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, UUID> {

    /**
     * Find URL by generated short code.
     */
    Optional<Url> findByShortCode(String shortCode);

    /**
     * Find URL by custom alias.
     */
    Optional<Url> findByCustomAlias(String customAlias);

    /**
     * Check whether short code already exists.
     */
    boolean existsByShortCode(String shortCode);

    /**
     * Check whether custom alias already exists.
     */
    boolean existsByCustomAlias(String customAlias);

    /**
     * Get all URLs created by a user email.
     */
    List<Url> findByUserEmail(String userEmail);

    /**
     * Get only active URLs created by a user.
     */
    List<Url> findByUserEmailAndActiveTrue(String userEmail);

    /**
     * Count URLs created by a user.
     */
    long countByUserEmail(String userEmail);

    /**
     * Count active URLs created by a user.
     */
    long countByUserEmailAndActiveTrue(String userEmail);
}
