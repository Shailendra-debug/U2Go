package com.skushwaha.u2go.Controller;

import com.skushwaha.u2go.Service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final UrlService urlService;

    /**
     * Redirect short URL to original URL.
     *
     * Example:
     * GET https://u2go.in/aB72xK9
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode
    ) {


        System.out.println("ewrfwerf"+shortCode);

        String originalUrl =
                urlService.getOriginalUrlAndIncrementClick(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                java.net.URI.create(originalUrl)
        );

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }
}