package com.mypay.loan_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    LoanApplication application;

    @Column(name = "account_id", nullable = false)
    String accountId;

    @Column(name = "principal_amount", nullable = false)
    BigDecimal principalAmount;

    @Column(name = "interest_rate", nullable = false)
    BigDecimal interestRate; // Lãi suất % / năm

    @Column(name = "outstanding_principal", nullable = false)
    BigDecimal outstandingPrincipal; // Dư nợ gốc hiện tại

    @Column(name = "debt_group", nullable = false)
    @Builder.Default
    Integer debtGroup = 1; // 1: Đủ tiêu chuẩn, 2: Cần chú ý, 3: Dưới tiêu chuẩn, 4: Nghi ngờ, 5: Mất vốn

    @Column(nullable = false)
    String status; // ACTIVE, CLOSED, DEFAULTED

    @Column(name = "disbursed_at")
    LocalDateTime disbursedAt;
}