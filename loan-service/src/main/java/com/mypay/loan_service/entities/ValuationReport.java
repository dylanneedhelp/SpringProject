package com.mypay.loan_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "valuation_reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValuationReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    LoanApplication loanApplication;

    @Column(name = "appraiser_id", nullable = false)
    String appraiserId; // Account ID của nhân viên Admin/Thẩm định viên

    @Column(name = "valuation_method", nullable = false)
    String valuationMethod; // DIRECT_COMPARISON, RESIDUAL_METHOD, INCOME_APPROACH

    @Column(name = "estimated_value", nullable = false)
    BigDecimal estimatedValue; // Giá trị tài sản/dòng tiền ước tính

    @Column(name = "approved_amount", nullable = false)
    BigDecimal approvedAmount; // Hạn mức tín dụng tối đa được cấp

    @Column(columnDefinition = "TEXT")
    String notes; // Báo cáo chi tiết

    @Column(name = "created_at")
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
}