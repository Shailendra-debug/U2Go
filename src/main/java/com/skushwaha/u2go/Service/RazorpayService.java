package com.skushwaha.u2go.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.skushwaha.u2go.DTO.RazorpayOrderResponse;
import com.skushwaha.u2go.Entity.AppId;
import com.skushwaha.u2go.Entity.PlanPricing;
import com.skushwaha.u2go.Entity.UrlPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    /**
     * Creates a Razorpay Order.
     * @return The Order ID to be sent to the frontend.
     */
    public RazorpayOrderResponse RazorpayOrderResponse(UrlPlan plan, String userEmail,UUID urlId) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        Long amount = PlanPricing.getAmountInPaise(plan);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "rcpt_" + UUID.randomUUID().toString().substring(0, 8));

        // Optional notes
        JSONObject notes = new JSONObject();
        notes.put("email", userEmail);
        notes.put("plan", plan.name());
        notes.put("urlId", urlId);
        notes.put("appId", AppId.U2GO);
        orderRequest.put("notes", notes);
        Order order = razorpayClient.orders.create(orderRequest);
        log.info("Razorpay order created: {} for email: {}", order.get("id"), userEmail);
        return new RazorpayOrderResponse(order.get("id"),amount,"INR",razorpayKeyId);
    }

    /**
     * Verifies the payment signature sent by the frontend.
     */
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            return Utils.verifyPaymentSignature(options, razorpayKeySecret);
        } catch (RazorpayException e) {
            log.error("Razorpay signature verification failed for order: {}", orderId, e);
            return false;
        }
    }
}