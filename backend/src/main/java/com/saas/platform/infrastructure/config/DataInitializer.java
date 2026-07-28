package com.saas.platform.infrastructure.config;

import com.saas.platform.tenant.context.TenantContext;
import com.saas.platform.user.entity.Role;
import com.saas.platform.user.entity.RoleEnum;
import com.saas.platform.user.entity.User;
import com.saas.platform.user.entity.UserStatus;
import com.saas.platform.user.repository.RoleRepository;
import com.saas.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Arrays.stream(RoleEnum.values()).forEach(roleEnum -> {
            if (roleRepository.findByName(roleEnum).isEmpty()) {
                Role role = Role.builder()
                        .name(roleEnum)
                        .description("System default role: " + roleEnum.name())
                        .build();
                roleRepository.save(role);
                log.info("Initialized default system role: {}", roleEnum.name());
            }
        });

        // Seed default CMO admin user if not present
        String cmoEmail = "dr.vishnu@ayushhealth.com";
        if (userRepository.findByEmail(cmoEmail).isEmpty()) {
            Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(RoleEnum.ROLE_ADMIN).description("Admin").build()));

            User defaultCmo = User.builder()
                    .tenantId(TenantContext.DEFAULT_TENANT_ID)
                    .email(cmoEmail)
                    .passwordHash(passwordEncoder.encode("password123"))
                    .firstName("Dr. Vishnu")
                    .lastName("Tiwari")
                    .phoneNumber("+919876543210")
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(defaultCmo);
            log.info("Initialized default CMO user: {} with tenantId: {}", cmoEmail, TenantContext.DEFAULT_TENANT_ID);
        }
    }
}
