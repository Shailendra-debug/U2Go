package com.skushwaha.u2go.DTO;

public record PaymentOrderResponse (
        String razorpayOrderId,
        Long amount,
        String currency,
        String razorpayKeyId
) implements UrlResponseCreat {
}