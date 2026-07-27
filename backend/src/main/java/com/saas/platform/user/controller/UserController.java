package com.saas.platform.user.controller;

import com.saas.platform.common.dto.ApiResponse;
import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.tenant.context.TenantContext;
import com.saas.platform.user.dto.UserCreateDto;
import com.saas.platform.user.dto.UserResponseDto;
import com.saas.platform.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User administration endpoints")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN')")
    @Operation(summary = "Create user within organization")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody UserCreateDto request) {
        UserResponseDto user = userService.createUser(request);
        return new ResponseEntity<>(ApiResponse.success(user, "User created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Get user details")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable UUID id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'MANAGER')")
    @Operation(summary = "List tenant users")
    public ResponseEntity<ApiResponse<PageResponse<UserResponseDto>>> getTenantUsers(
            @RequestParam(required = false) UUID tenantId,
            Pageable pageable) {
        UUID effectiveTenantId = tenantId != null ? tenantId : TenantContext.getTenantId();
        PageResponse<UserResponseDto> users = userService.getUsersByTenant(effectiveTenantId, pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN')")
    @Operation(summary = "Update user status")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUserStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        UserResponseDto updated = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "User status updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN')")
    @Operation(summary = "Deactivate user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deactivated"));
    }
}
