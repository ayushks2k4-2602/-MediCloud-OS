package com.saas.platform.user.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.user.dto.UserCreateDto;
import com.saas.platform.user.dto.UserResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserCreateDto request);
    UserResponseDto getUserById(UUID id);
    UserResponseDto getUserByEmail(String email);
    PageResponse<UserResponseDto> getUsersByTenant(UUID tenantId, Pageable pageable);
    UserResponseDto updateUserStatus(UUID id, String status);
    void deleteUser(UUID id);
}
