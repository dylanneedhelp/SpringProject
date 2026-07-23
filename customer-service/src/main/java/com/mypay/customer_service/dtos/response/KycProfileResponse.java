package com.mypay.customer_service.dtos.response;

import com.mypay.customer_service.entities.Customer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KycProfileResponse {
    String id;
    String idCardNumber;
    String frontImageUrl;
    String backImageUrl;
    String faceVideoUrl;
    String status;
    String rejectionReason;
    LocalDateTime verifiedAt;
}
