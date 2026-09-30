package com.skushwaha.u2go.DTO;

public record RazorpayOrderResponse(
        String orderId,
        Long amount,
        String currency,
        String keyId
) {
}
