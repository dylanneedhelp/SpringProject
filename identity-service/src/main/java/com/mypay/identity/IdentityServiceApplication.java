package com.mypay.identity;

import com.mypay.identity.entities.Role;
import com.mypay.identity.entities.User;
import com.mypay.identity.enums.AccountStatus;
import com.mypay.identity.repositories.RoleRepository;
import com.mypay.identity.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class IdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
	}
	@Bean
	ApplicationRunner dataSeeding(RoleRepository roleRepository, UserRepository userRepository) {
		return args -> {
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			if (roleRepository.count() == 0) {
				roleRepository.save(Role.builder().roleId("ADMIN").roleName("Quản trị viên").description("Toàn quyền hệ thống").build());
				roleRepository.save(Role.builder().roleId("MANAGER").roleName("Quản lý").description("Quản lý vận hành dịch vụ").build());
				roleRepository.save(Role.builder().roleId("USER").roleName("Người dùng").description("Khách hàng sử dụng ví MyPay").build());
				System.out.println(">>> Đã nạp thành công hệ thống quyền chuẩn C# Identity!");
			}
			if (userRepository.count() == 0) {
				Role adminRole = roleRepository.findById("ADMIN").orElse(null);
				Role managerRole = roleRepository.findById("MANAGER").orElse(null);
				Role userRole = roleRepository.findById("USER").orElse(null);


				Set<Role> adminRoles = new HashSet<>();
				adminRoles.add(adminRole);
				adminRoles.add(managerRole);
				adminRoles.add(userRole);
				User admin = User.builder()
						.username("admin")
						.password(encoder.encode("Aa@123"))
						.email("admin@mypay.com")
						.fullName("Super Admin MyPay")
						.isActive(true)
						.status(AccountStatus.ACTIVE)
						.roles(adminRoles)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.build();
				userRepository.save(admin);



				System.out.println(">>> Đã nạp thành công 3 tài khoản mặc định (admin/admin123, manager/manager123, user/user123)!");
			}
		};
	}

}
