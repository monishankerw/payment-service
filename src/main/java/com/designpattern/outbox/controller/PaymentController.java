package com.designpattern.outbox.controller;


import com.designpattern.outbox.dto.PaymentRequest;
import com.designpattern.outbox.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest request) {
        String transactionId = paymentService.createPayment(request);
        return ResponseEntity.ok("Payment created successfully. TransactionId = " + transactionId);
    }
}