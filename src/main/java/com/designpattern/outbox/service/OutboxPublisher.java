package com.designpattern.outbox.service;


import com.designpattern.outbox.entity.OutboxEvent;
import com.designpattern.outbox.enums.OutboxStatus;
import com.designpattern.outbox.repository.OutboxEventRepository;
import com.designpattern.outbox.repository.PaymentKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {
    private final OutboxEventRepository outboxEventRepository;
    private final PaymentKafkaProducer kafkaProducer;

    @Scheduled(fixedDelayString = "${outbox.scheduler.fixed-delay:5000}")
    public void publishEvents() {
        List<OutboxEvent> events = outboxEventRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW);
        for (OutboxEvent event : events) {
            try {
                log.info("Publishing event: {}", event.getEventId());
                kafkaProducer.publish(event.getEventId(), event.getPayload());
                markPublished(event);
            } catch (Exception e) {
                log.error("Failed to publish event: {}", event.getEventId(), e);
                markFailed(event);
            }
        }
    }

    @Transactional
    public void markPublished(OutboxEvent event) {
        event.setStatus(OutboxStatus.PUBLISHED);
        event.setProcessedAt(LocalDateTime.now());
        outboxEventRepository.save(event);
    }

    @Transactional
    public void markFailed(OutboxEvent event) {
        event.setRetryCount(event.getRetryCount() + 1);
        event.setStatus(OutboxStatus.FAILED);
        outboxEventRepository.save(event);
    }
}