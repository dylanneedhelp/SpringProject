package com.mypay.loan_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_applications")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "account_id", nullable = false)
    String accountId;

    @Column(name = "requested_amount", nullable = false)
    BigDecimal requestedAmount;

    @Column(name = "term_months", nullable = false)
    Integer termMonths;

    @Column(columnDefinition = "TEXT")
    String purpose;

    @Column(nullable = false)
    String status;

    @Column(name = "created_at")
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}