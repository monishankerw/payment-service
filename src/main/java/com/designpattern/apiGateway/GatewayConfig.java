package com.designpattern.apiGateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("order-service", r -> r
                        .path("/orders/**")
                        .filters(f -> f
                                .stripPrefix(1)
                        )
                        .uri("http://localhost:8081"))

                .route("inventory-service", r -> r
                        .path("/inventory/**")
                        .uri("http://localhost:8082"))

                .build();
    }
}