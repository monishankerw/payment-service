package com.designpattern.outbox.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentConsumer {

    @KafkaListener(topics = "payment-topic", groupId = "payment-consumer-group")
    public void consume(String message) {
        log.info("Payment event received: {}", message);
        // Business processing
    }
}