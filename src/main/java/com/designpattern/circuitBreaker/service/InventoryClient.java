package com.designpattern.circuitBreaker.service;


import com.designpattern.circuitBreaker.dto.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryClient {

    public Inventory getStock(String skuId) {

        System.out.println(
                "Calling Inventory Service for SKU: " + skuId
        );

        return new Inventory(
                skuId,
                100,
                true
        );
    }
}