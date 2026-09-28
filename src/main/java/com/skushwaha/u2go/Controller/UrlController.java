package com.skushwaha.u2go.Controller;

import com.skushwaha.u2go.DTO.CreateUrlRequest;
import com.skushwaha.u2go.DTO.UrlResponse;
import com.skushwaha.u2go.Service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    /**
     * Create a short URL.
     *
     * POST /api/urls
     */
    @PostMapping
        public ResponseEntity<UrlResponse> createUrl(
            @Valid @RequestBody CreateUrlRequest request
    ) {

        UrlResponse response = urlService.createUrl(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    /**
     * Get URL information using short code.
     *
     * GET /api/urls/{shortCode}
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlResponse> getUrl(
            @PathVariable String shortCode
    ) {

        UrlResponse response =
                urlService.getUrlByShortCode(shortCode);

        return ResponseEntity.ok(response);
    }


    /**
     * Get all URLs created by a user email.
     *
     * GET /api/urls/user/{email}
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<List<UrlResponse>> getUserUrls(
            @PathVariable String email
    ) {

        List<UrlResponse> urls =
                urlService.getUrlsByUserEmail(email);

        return ResponseEntity.ok(urls);
    }
}
