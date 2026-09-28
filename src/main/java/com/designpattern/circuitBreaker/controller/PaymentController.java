package com.designpattern.circuitBreaker.controller;


import com.designpattern.circuitBreaker.dto.Inventory;
import com.designpattern.circuitBreaker.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/inventory/{skuId}")
    public Inventory checkInventory(
            @PathVariable String skuId
    ) {

        return paymentService.checkInventory(skuId);
    }
}