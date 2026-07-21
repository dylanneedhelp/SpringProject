package com.mypay.api_gateway.config;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GatewayConfig {
    private final KeyResolver userIpKeyResolver;
    @Value("${mypay.services.identity-uri}")
    private String identityUri;
    @Value("${mypay.services.customer-uri}")
    private String customerUri;
    @Value("${mypay.services.loan-uri}")
    private String loanUri;
    @Value("${mypay.services.notification-uri}")
    private String notificationUri;

    public GatewayConfig(KeyResolver userIpKeyResolver) {
        this.userIpKeyResolver = userIpKeyResolver;
    }

    // Cấu hình thông số cho Redis (Tốc độ hồi 2, Tối đa 5, Chi phí 1)
    @Bean
    @Primary
    public RedisRateLimiter defaultRateLimiter() {
        return new RedisRateLimiter(2, 5, 1);
    }
    @Bean
    public RedisRateLimiter strictRateLimiter(){
        return new RedisRateLimiter(1,2,1);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("test-mock-api", r -> r
                        .path("/todos/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setRateLimiter(defaultRateLimiter());
                            config.setKeyResolver(userIpKeyResolver);
                        }))
                        .uri("https://jsonplaceholder.typicode.com")
                )
                .route("identity-service", r -> r
                        .path("/api/v1/auth/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setRateLimiter(strictRateLimiter());
                            config.setKeyResolver(userIpKeyResolver);
                        }))
                        .uri(identityUri)
                )
                .route("customer-service", r -> r
                        .path("/api/v1/customers/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setRateLimiter(defaultRateLimiter());
                            config.setKeyResolver(userIpKeyResolver);
                        }))
                        .uri(customerUri)
                )
                .route("loan-service", r -> r
                        .path("/api/v1/loans/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setRateLimiter(defaultRateLimiter());
                            config.setKeyResolver(userIpKeyResolver);
                        }))
                        .uri(loanUri)
                ).route("notification-service", r -> r
                        .path("/api/v1/notifications/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setRateLimiter(defaultRateLimiter());
                            config.setKeyResolver(userIpKeyResolver);
                        }))
                        .uri(notificationUri)
                )
                .build();
    }
}