package com.saas.platform.auth.service;

import com.saas.platform.auth.dto.AuthResponse;
import com.saas.platform.auth.dto.LoginRequest;
import com.saas.platform.auth.dto.RefreshTokenRequest;
import com.saas.platform.auth.dto.RegisterRequest;
import com.saas.platform.auth.entity.RefreshToken;
import com.saas.platform.auth.repository.RefreshTokenRepository;
import com.saas.platform.auth.security.JwtTokenProvider;
import com.saas.platform.auth.security.UserPrincipal;
import com.saas.platform.common.exception.ResourceNotFoundException;
import com.saas.platform.common.exception.UnauthorizedException;
import com.saas.platform.tenant.context.TenantContext;
import com.saas.platform.tenant.dto.TenantRequestDto;
import com.saas.platform.tenant.dto.TenantResponseDto;
import com.saas.platform.tenant.entity.SubscriptionPlan;
import com.saas.platform.tenant.service.TenantService;
import com.saas.platform.user.dto.UserCreateDto;
import com.saas.platform.user.dto.UserResponseDto;
import com.saas.platform.user.entity.RoleEnum;
import com.saas.platform.user.entity.User;
import com.saas.platform.user.repository.UserRepository;
import com.saas.platform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TenantService tenantService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${app.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        UUID tenantId = request.getTenantId() != null ? request.getTenantId() : TenantContext.getTenantId();
        TenantContext.setTenantId(tenantId);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshTokenStr = tokenProvider.generateRefreshToken(userPrincipal.getId(), tenantId);

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tenantId(tenantId)
                .token(refreshTokenStr)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        UserResponseDto userDto = userService.getUserById(userPrincipal.getId());

        log.info("User logged in: email={}, tenantId={}", request.getEmail(), tenantId);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .tokenType("Bearer")
                .user(userDto)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse registerTenantAndOwner(RegisterRequest request) {
        TenantRequestDto tenantRequest = TenantRequestDto.builder()
                .name(request.getOrganizationName())
                .domain(request.getDomain())
                .plan(SubscriptionPlan.FREE)
                .build();
        TenantResponseDto tenant = tenantService.createTenant(tenantRequest);

        UserCreateDto userRequest = UserCreateDto.builder()
                .tenantId(tenant.getId())
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(Set.of(RoleEnum.ROLE_ORG_OWNER))
                .build();
        UserResponseDto user = userService.createUser(userRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .tenantId(tenant.getId())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        return login(loginRequest);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        if (!tokenProvider.validateToken(token)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh token not found"));

        if (storedToken.getRevoked() || storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token expired or revoked");
        }

        User user = storedToken.getUser();
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        String newAccessToken = tokenProvider.generateAccessToken(user.getId(), user.getTenantId(), user.getEmail(), "ROLE_ORG_OWNER");
        String newRefreshTokenStr = tokenProvider.generateRefreshToken(user.getId(), user.getTenantId());

        RefreshToken newRefreshToken = RefreshToken.builder()
                .user(user)
                .tenantId(user.getTenantId())
                .token(newRefreshTokenStr)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        UserResponseDto userDto = userService.getUserById(user.getId());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenStr)
                .tokenType("Bearer")
                .user(userDto)
                .build();
    }

    @Override
    @Transactional
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            if (tokenProvider.validateToken(jwt)) {
                UUID userId = tokenProvider.getUserIdFromToken(jwt);
                refreshTokenRepository.deleteByUserId(userId);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("Not authenticated");
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId());
    }
}
