package com.mypay.identity.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_logs")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;

    @Column(name = "login_time")
    LocalDateTime loginTime = LocalDateTime.now();

    @Column(name = "ip_address")
    String ipAddress;

    @Column(name = "device_info")
    String deviceInfo;

    @Column(name = "is_success")
    boolean isSuccess;

    String failureReason;
}
