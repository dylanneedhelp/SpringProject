package com.mypay.loan_service.clients;

import com.mypay.loan_service.dtos.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(name = "customer-service", url = "${mypay.services.customer-uri:http://localhost:8082}")
public interface CustomerServiceClient {

    // 1. Lấy thông tin ví dựa vào accountId
    @GetMapping("/api/v1/internal/wallets/account/{accountId}")
    ApiResponse<Map<String, Object>> getWalletByAccountId(@PathVariable("accountId") String accountId);

    // 2. Gọi API cộng tiền vào ví
    @PostMapping("/api/v1/internal/wallets/{walletId}/add")
    ApiResponse<String> addBalance(@PathVariable("walletId") String walletId, @RequestBody Map<String, BigDecimal> request);
}