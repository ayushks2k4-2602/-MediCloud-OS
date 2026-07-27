package com.saas.platform.auth.service;

import com.saas.platform.auth.dto.AuthResponse;
import com.saas.platform.auth.dto.LoginRequest;
import com.saas.platform.auth.dto.RefreshTokenRequest;
import com.saas.platform.auth.dto.RegisterRequest;
import com.saas.platform.user.dto.UserResponseDto;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse registerTenantAndOwner(RegisterRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(String token);
    UserResponseDto getCurrentUser();
}
