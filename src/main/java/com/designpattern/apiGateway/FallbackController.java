package com.designpattern.apiGateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/orders")
    public String orderFallback() {

        return "Order service is temporarily unavailable";
    }
}