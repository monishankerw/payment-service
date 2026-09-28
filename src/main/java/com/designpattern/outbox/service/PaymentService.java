package com.designpattern.outbox.service;


import com.designpattern.outbox.dto.PaymentEvent;
import com.designpattern.outbox.dto.PaymentRequest;
import com.designpattern.outbox.entity.OutboxEvent;
import com.designpattern.outbox.entity.Payment;
import com.designpattern.outbox.enums.OutboxStatus;
import com.designpattern.outbox.repository.OutboxEventRepository;
import com.designpattern.outbox.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public String createPayment(PaymentRequest request) {

        // 1. Generate Transaction ID
        String transactionId = UUID.randomUUID().toString();
        // 2. Save payment
        Payment payment = Payment.builder().transactionId(transactionId).customerId(request.getCustomerId()).amount(request.getAmount()).status("SUCCESS").createdAt(LocalDateTime.now()).build();
        paymentRepository.save(payment);
        // 3. Create event
        PaymentEvent event = PaymentEvent.builder().eventId(UUID.randomUUID().toString()).transactionId(transactionId).customerId(request.getCustomerId()).amount(request.getAmount()).status("SUCCESS").build();
        // 4. Convert event to JSON
        String payload;
        payload = objectMapper.writeValueAsString(event);
        // 5. Save event in Outbox table
        OutboxEvent outboxEvent = OutboxEvent.builder().eventId(event.getEventId()).eventType("PAYMENT_SUCCESS").payload(payload).status(OutboxStatus.NEW).retryCount(0).createdAt(LocalDateTime.now()).build();
        outboxEventRepository.save(outboxEvent);
        return transactionId;
    }
}