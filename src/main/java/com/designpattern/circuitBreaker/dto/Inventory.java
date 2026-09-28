package com.designpattern.circuitBreaker.dto;

public record Inventory(
        String skuId,
        int availableQuantity,
        boolean available
) {

    public static Inventory unavailable(String skuId) {

        return new Inventory(
                skuId,
                0,
                false
        );
    }
}