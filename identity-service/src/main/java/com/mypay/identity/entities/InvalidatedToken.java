package com.mypay.identity.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;
@Entity
@Table(name = "invalidated_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidatedToken {
    @Id
    String id;

    @Column(nullable = false)
    Date expiryTime;
}