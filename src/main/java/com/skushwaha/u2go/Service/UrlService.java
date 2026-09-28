package com.skushwaha.u2go.Service;

import com.skushwaha.u2go.DTO.CreateUrlRequest;
import com.skushwaha.u2go.DTO.UrlResponse;
import com.skushwaha.u2go.Email.EmailService;
import com.skushwaha.u2go.Entity.Url;
import com.skushwaha.u2go.Entity.UrlPlan;
import com.skushwaha.u2go.Repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UrlService {

    private final UrlRepository urlRepository;
    private final EmailService emailService;
    private final String BASE_URL="http://localhost:8080/";

    /**
     * Create a new short URL.
     */

    public UrlResponse createUrl(CreateUrlRequest request) {

        String shortCode = generateUniqueShortCode();

        Url url = Url.builder()
                .originalUrl(request.originalUrl())
                .shortCode(shortCode)
                .customAlias(request.customAlias())
                .userEmail(request.userEmail())
                .plan(UrlPlan.FREE)
                .build();

        Url savedUrl = urlRepository.save(url);

        emailService.sendShortUrlEmail(request.userEmail(),request.originalUrl(),BASE_URL+shortCode,null);


        return mapToResponse(savedUrl);
    }


    /**
     * Get URL by short code.
     */
    @Transactional(readOnly = true)
    public UrlResponse getUrlByShortCode(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("Short URL not found")
                );

        if (!url.isAvailable()) {
            throw new RuntimeException("Short URL is inactive or expired");
        }

        return mapToResponse(url);
    }


    /**
     * Get all URLs created by an email.
     */
    @Transactional(readOnly = true)
    public List<UrlResponse> getUrlsByUserEmail(String email) {

        return urlRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    /**
     * Generate a unique short code.
     */
    private String generateUniqueShortCode() {

        String shortCode;

        do {
            shortCode = generateShortCode();
        } while (urlRepository.existsByShortCode(shortCode));

        return shortCode;
    }


    /**
     * Generate a 7-character short code.
     */
    private String generateShortCode() {

        String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "abcdefghijklmnopqrstuvwxyz" +
                        "0123456789";

        StringBuilder code = new StringBuilder(7);

        for (int i = 0; i < 7; i++) {

            int index = (int) (Math.random() * characters.length());

            code.append(characters.charAt(index));
        }

        return code.toString();
    }

    @Transactional
    public String getOriginalUrlAndIncrementClick(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("Short URL not found")
                );

        if (!url.isAvailable()) {
            throw new RuntimeException(
                    "Short URL is inactive or expired"
            );
        }

        url.incrementClickCount();

        return url.getOriginalUrl();
    }

    /**
     * Convert Entity → Response DTO.
     */
    private UrlResponse mapToResponse(Url url) {

        String shortUrl =
                BASE_URL + url.getShortCode();

        return new UrlResponse(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortCode(),
                shortUrl,
                url.getCustomAlias(),
                url.getUserEmail(),
                url.getPlan(),
                url.getClickCount(),
                url.getActive(),
                url.getCreatedAt(),
                url.getUpdatedAt(),
                url.getExpiresAt()
        );
    }
}
