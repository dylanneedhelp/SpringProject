package com.mypay.loan_service.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppraisalRequest {
    @NotBlank(message = "Phương pháp định giá không được để trống")
    private String valuationMethod;

    @NotNull(message = "Giá trị ước tính không được để trống")
    private BigDecimal estimatedValue;

    @NotNull(message = "Hạn mức duyệt vay không được để trống")
    private BigDecimal approvedAmount;

    private String notes;
}