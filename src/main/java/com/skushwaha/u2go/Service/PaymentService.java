package com.skushwaha.u2go.Service;

import com.razorpay.RazorpayException;
import com.skushwaha.u2go.DTO.PaymentOrderResponse;
import com.skushwaha.u2go.DTO.RazorpayOrderResponse;
import com.skushwaha.u2go.Entity.*;
import com.skushwaha.u2go.Repository.PaymentRepository;
import com.skushwaha.u2go.Repository.UrlRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UrlRepository urlRepository;
    private final RazorpayService razorpayService;

    public PaymentOrderResponse createPayment(
            UUID urlId,
            String userEmail
    ) throws RazorpayException {

        Url url = urlRepository.findById(urlId)
                .orElseThrow(() ->
                        new EntityNotFoundException("URL not found"));

        if (url.getPlan() == UrlPlan.FREE) {
            throw new IllegalStateException(
                    "Payment is not required for FREE plan"
            );
        }

        // Create Razorpay order using your existing service
        RazorpayOrderResponse razorpayOrder =
                razorpayService.RazorpayOrderResponse(
                        url.getPlan(),userEmail
                );

        Payment payment = Payment.builder()
                .razorpayOrderId(razorpayOrder.orderId())
                .userEmail(userEmail)
                .amount(razorpayOrder.amount())
                .status(PaymentStatus.CREATED)
                .appId(AppId.U2GO)
                .plan(url.getPlan())
                .url(url)
                .build();

        paymentRepository.save(payment);

        return new PaymentOrderResponse(
                razorpayOrder.orderId(),
                razorpayOrder.amount(),
                razorpayOrder.currency(),
                razorpayOrder.keyId()
        );
    }

}