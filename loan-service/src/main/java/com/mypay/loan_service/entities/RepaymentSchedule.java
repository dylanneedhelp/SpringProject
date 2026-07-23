package com.mypay.loan_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "repayment_schedules")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepaymentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    Loan loan;

    @Column(name = "installment_number", nullable = false)
    Integer installmentNumber;

    @Column(name = "due_date", nullable = false)
    LocalDate dueDate;

    @Column(name = "principal_due", nullable = false)
    BigDecimal principalDue;

    @Column(name = "interest_due", nullable = false)
    BigDecimal interestDue;

    @Column(name = "penalty_fee", nullable = false)
    @Builder.Default
    BigDecimal penaltyFee = BigDecimal.ZERO;

    @Column(nullable = false)
    String status; // UNPAID, PAID, OVERDUE

    @Column(name = "paid_at")
    LocalDateTime paidAt;
}