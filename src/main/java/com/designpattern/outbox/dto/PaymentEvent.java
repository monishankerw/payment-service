package com.designpattern.outbox.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {

    private String eventId;

    private String transactionId;

    private String customerId;

    private BigDecimal amount;

    private String status;
}