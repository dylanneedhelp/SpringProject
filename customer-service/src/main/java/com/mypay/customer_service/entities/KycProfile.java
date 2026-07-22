package com.mypay.customer_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_profiles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    Customer customer;

    @Column(name = "id_card_number", unique = true, nullable = false)
    String idCardNumber;

    @Column(name = "front_image_url", columnDefinition = "TEXT")
    String frontImageUrl;

    @Column(name = "back_image_url", columnDefinition = "TEXT")
    String backImageUrl;

    @Column(name = "face_video_url", columnDefinition = "TEXT")
    String faceVideoUrl;

    @Column(nullable = false)
    String status; // PENDING, APPROVED, REJECTED

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    String rejectionReason;

    @Column(name = "verified_at")
    LocalDateTime verifiedAt;
}