package com.mypay.loan_service.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanApplyRequest {
    @NotNull(message = "Số tiền vay không được để trống")
    @Min(value = 1000000, message = "Số tiền vay tối thiểu là 1,000,000 VND")
    private BigDecimal requestedAmount;

    @NotNull(message = "Kỳ hạn không được để trống")
    private Integer termMonths;

    @NotBlank(message = "Mục đích vay không được để trống")
    private String purpose;
}