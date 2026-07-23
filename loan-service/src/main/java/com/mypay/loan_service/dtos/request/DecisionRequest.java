package com.mypay.loan_service.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DecisionRequest {
    @NotNull
    private Boolean isApproved;

    private BigDecimal interestRate; // Chỉ bắt buộc nếu duyệt (isApproved = true)

    private String rejectionReason; // Chỉ bắt buộc nếu từ chối (isApproved = false)
}