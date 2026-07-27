package com.saas.platform.infrastructure.config;

import com.saas.platform.user.entity.Role;
import com.saas.platform.user.entity.RoleEnum;
import com.saas.platform.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

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
    }
}
