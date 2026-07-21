package com.mypay.identity.repositories;

import com.mypay.identity.entities.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
