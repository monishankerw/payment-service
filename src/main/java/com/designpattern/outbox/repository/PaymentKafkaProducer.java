package com.designpattern.outbox.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void publish(String eventId, String payload) {
        kafkaTemplate.send("payment-topic", eventId, payload).whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Kafka publish failed. eventId={}", eventId, exception);
            } else {
                log.info("Kafka event published. eventId={}", eventId);
            }
        });
    }
}