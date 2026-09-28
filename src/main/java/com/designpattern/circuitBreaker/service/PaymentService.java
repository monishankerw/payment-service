package com.designpattern.circuitBreaker.service;

import com.designpattern.circuitBreaker.dto.Inventory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final InventoryClient inventoryClient;

    public PaymentService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @CircuitBreaker(
            name = "inventoryService",
            fallbackMethod = "inventoryFallback"
    )
    public Inventory checkInventory(String skuId) {

        System.out.println(
                "Circuit Breaker: Calling Inventory Service"
        );

        return inventoryClient.getStock(skuId);
    }

    public Inventory inventoryFallback(
            String skuId,
            Throwable throwable
    ) {

        System.out.println(
                "Circuit Breaker Fallback triggered"
        );

        System.out.println(
                "Reason: " + throwable.getMessage()
        );

        return Inventory.unavailable(skuId);
    }
}