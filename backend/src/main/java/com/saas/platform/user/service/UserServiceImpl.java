package com.saas.platform.user.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.common.exception.BadRequestException;
import com.saas.platform.common.exception.ResourceNotFoundException;
import com.saas.platform.tenant.context.TenantContext;
import com.saas.platform.user.dto.UserCreateDto;
import com.saas.platform.user.dto.UserResponseDto;
import com.saas.platform.user.entity.*;
import com.saas.platform.user.repository.RoleRepository;
import com.saas.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto request) {
        UUID tenantId = request.getTenantId() != null ? request.getTenantId() : TenantContext.getTenantId();

        if (userRepository.existsByTenantIdAndEmail(tenantId, request.getEmail())) {
            throw new BadRequestException("User with email '" + request.getEmail() + "' already exists");
        }

        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (RoleEnum roleEnum : request.getRoles()) {
                Role role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleEnum.name()));
                roles.add(role);
            }
        } else {
            Role defaultRole = roleRepository.findByName(RoleEnum.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_EMPLOYEE"));
            roles.add(defaultRole);
        }

        User user = User.builder()
                .tenantId(tenantId)
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created: id={}, email={}", savedUser.getId(), savedUser.getEmail());
        return mapToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        UUID tenantId = TenantContext.getTenantId();
        User user = userRepository.findByTenantIdAndEmail(tenantId, email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponseDto> getUsersByTenant(UUID tenantId, Pageable pageable) {
        UUID effectiveTenantId = tenantId != null ? tenantId : TenantContext.getTenantId();
        Page<UserResponseDto> page = userRepository.findByTenantId(effectiveTenantId, pageable).map(this::mapToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserStatus(UUID id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setStatus(UserStatus.valueOf(status.toUpperCase()));
        User updated = userRepository.save(user);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    public UserResponseDto mapToDto(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return UserResponseDto.builder()
                .id(user.getId())
                .tenantId(user.getTenantId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .twoFactorEnabled(user.getTwoFactorEnabled())
                .roles(roleNames)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
