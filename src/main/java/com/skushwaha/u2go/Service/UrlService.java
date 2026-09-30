package com.skushwaha.u2go.Service;

import com.razorpay.RazorpayException;
import com.skushwaha.u2go.DTO.*;
import com.skushwaha.u2go.Email.EmailService;
import com.skushwaha.u2go.Entity.PlanPricing;
import com.skushwaha.u2go.Entity.Url;
import com.skushwaha.u2go.Entity.UrlPlan;
import com.skushwaha.u2go.Repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UrlService {

    private final UrlRepository urlRepository;
    private final EmailService emailService;
    private final PaymentService paymentService;
    private final String BASE_URL="http://localhost:8080/";

    /**
     * Create a new short URL.
     */


    @Transactional
    public UrlResponseCreat createUrl(CreateUrlRequest request) throws RazorpayException {

        UrlPlan plan = request.urlPlan() != null
                ? request.urlPlan()
                : UrlPlan.FREE;

        String shortCode = generateUniqueShortCode();

        Url url = Url.builder()
                .originalUrl(request.originalUrl())
                .shortCode(shortCode)
                .customAlias(request.customAlias())
                .userEmail(request.userEmail())
                .plan(plan)
                .expiresAt(calculateExpiry(plan))
                .build();

        Url savedUrl = urlRepository.save(url);

        if (plan != UrlPlan.FREE) {
            return paymentService.createPayment(savedUrl.getId(),request.userEmail());
        }

        return new FreeUrlResponse(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortCode(),
                BASE_URL+shortCode,
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

    @Async("emailTaskExecutor")
    public void sendShortUrlEmailAsync(
            String email,
            String originalUrl,
            String shortUrl
    ) {
        try {

            emailService.sendShortUrlEmail(
                    email,
                    originalUrl,
                    shortUrl,
                    null
            );

            log.info(
                    "Short URL email sent successfully. email={}, shortCode={}",
                    maskEmail(email),
                    shortUrl.substring(shortUrl.lastIndexOf("/") + 1)
            );

        } catch (Exception e) {

            // Email failure must NOT affect URL creation
            log.error(
                    "Failed to send short URL email. email={}, shortUrl={}",
                    maskEmail(email),
                    shortUrl,
                    e
            );
        }
    }

    private String maskEmail(String email) {

        if (email == null || !email.contains("@")) {
            return "***";
        }

        String[] parts = email.split("@", 2);

        String username = parts[0];

        if (username.length() <= 2) {
            return "***@" + parts[1];
        }

        return username.substring(0, 2)
                + "***@"
                + parts[1];
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

    // Add this to UrlService.java
    @Transactional
    public void upgradeUserPlan(String email, UrlPlan newPlan) {
        log.info("Upgrading plan for user: {} to {}", maskEmail(email), newPlan);

        // In a real app, you might have a User entity.
        // Here, we will update all existing URLs for this email.
        List<Url> userUrls = urlRepository.findByUserEmail(email);

        for (Url url : userUrls) {
            url.setPlan(newPlan);
            url.setExpiresAt(calculateExpiry(newPlan));
        }

        urlRepository.saveAll(userUrls);
    }

    private Instant calculateExpiry(UrlPlan plan) {
        if (plan == null || plan == UrlPlan.FREE) {
            return Instant.now().plus(7, ChronoUnit.DAYS); // Free trial = 7 days
        }
        int months = PlanPricing.getDurationInMonths(plan);
        return Instant.now().plus(months * 30L, ChronoUnit.DAYS); // Approximate months to days
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
