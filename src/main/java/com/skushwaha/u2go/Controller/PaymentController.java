package com.skushwaha.u2go.Controller;

import com.razorpay.RazorpayException;
import com.skushwaha.u2go.Entity.AppId;
import com.skushwaha.u2go.Entity.UrlPlan;
import com.skushwaha.u2go.Service.RazorpayService;
import com.skushwaha.u2go.Service.UrlService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;
    private final UrlService urlService;


    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody VerifyPaymentRequest request) {
        if (request.appId!=AppId.U2GO){
            return ResponseEntity.badRequest().body("Invalid payment signature.");
        }
        boolean isValid = razorpayService.verifyPaymentSignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        if (isValid) {
            // Payment is successful, upgrade the user's plan
            urlService.upgradeUserPlan(request.getEmail(), request.getPlan(),request.urlId,request.razorpayOrderId);
            return ResponseEntity.ok("Payment verified and plan upgraded successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid payment signature.");
        }
    }

    @PostMapping("/update/plan")
    public ResponseEntity<?>updatePlane(@RequestBody UpdatePlaneRequest request) throws RazorpayException {
        return ResponseEntity.ok(urlService.updatePlane(request.shortCode,request.plan));
    }

    // --- DTOs ---

    @Data
    public static class  UpdatePlaneRequest{
        private String shortCode;
        private UrlPlan plan;
    }

    @Data
    public static class CreateOrderRequest {
        private String email;
        private UrlPlan plan;
    }


    @Data
    public static class VerifyPaymentRequest {
        private String email;
        private UrlPlan plan;
        private UUID urlId;
        private String razorpayOrderId;
        private String razorpayPaymentId;
        private String razorpaySignature;
        private AppId appId;
    }
}