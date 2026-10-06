package com.skushwaha.u2go.Controller;

import com.razorpay.RazorpayException;
import com.skushwaha.u2go.DTO.CreateUrlRequest;
import com.skushwaha.u2go.DTO.UrlResponse;
import com.skushwaha.u2go.Service.UrlService;
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
        public ResponseEntity<?> createUrl(@RequestBody CreateUrlRequest request) throws RazorpayException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(urlService.createUrl(request));
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
    @GetMapping("/qr/users/{email}")
    public ResponseEntity<List<UrlResponse>> getUserQrs(
            @PathVariable String email
    ) {

        List<UrlResponse> urls =
                urlService.getQrByUserEmail(email);

        return ResponseEntity.ok(urls);
    }
}
