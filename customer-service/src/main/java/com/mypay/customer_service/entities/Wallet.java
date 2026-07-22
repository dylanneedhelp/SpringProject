package com.mypay.customer_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    Customer customer;

    @Column(nullable = false)
    @Builder.Default
    BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "frozen_balance", nullable = false)
    @Builder.Default
    BigDecimal frozenBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    String currency = "VND";

    String tier; // STANDARD, PREMIUM, VIP...

    @Column(nullable = false)
    String status; // ACTIVE, BLOCKED
}