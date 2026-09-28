package com.designpattern.outbox.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    private String customerId;

    private BigDecimal amount;
}