package com.skushwaha.u2go.Repository;


import com.skushwaha.u2go.Entity.Payment;
import com.skushwaha.u2go.Entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository
        extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    boolean existsByRazorpayOrderId(String razorpayOrderId);

    boolean existsByRazorpayPaymentId(String razorpayPaymentId);

    Optional<Payment> findByRazorpayOrderIdAndStatus(
            String razorpayOrderId,
            PaymentStatus status
    );

    List<Payment> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}