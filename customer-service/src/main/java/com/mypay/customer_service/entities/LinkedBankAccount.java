package com.mypay.customer_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "linked_bank_accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkedBankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
    Wallet wallet;

    @Column(name = "bank_code", nullable = false)
    String bankCode;

    @Column(name = "account_number", nullable = false)
    String accountNumber;

    @Column(name = "account_name", nullable = false)
    String accountName;

    @Column(name = "is_primary")
    @Builder.Default
    boolean isPrimary = false;

    @Column(name = "linked_at")
    @Builder.Default
    LocalDateTime linkedAt = LocalDateTime.now();
}