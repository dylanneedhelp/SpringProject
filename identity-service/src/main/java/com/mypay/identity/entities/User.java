package com.mypay.identity.entities;

import com.mypay.identity.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true, length = 50)
    String username;

    @Column(nullable = false)
    String password;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "avatar",columnDefinition = "TEXT")
    String avatar;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    boolean isActive = true;

    @Column(name = "is_locked")
    boolean isLocked = false;

    @Column(name = "failed_login_attempts")
    int failedLoginAttempts = 0;

    @Column(name = "lockout_end")
    LocalDateTime lockoutEnd;

    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AccountStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles;
}
