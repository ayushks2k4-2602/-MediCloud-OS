package com.saas.platform.tenant.controller;

import com.saas.platform.common.dto.ApiResponse;
import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.tenant.dto.TenantRequestDto;
import com.saas.platform.tenant.dto.TenantResponseDto;
import com.saas.platform.tenant.service.TenantService;
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
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant Management", description = "Multi-tenant organization management APIs")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Create a new tenant organization")
    public ResponseEntity<ApiResponse<TenantResponseDto>> createTenant(@Valid @RequestBody TenantRequestDto request) {
        TenantResponseDto tenant = tenantService.createTenant(request);
        return new ResponseEntity<>(ApiResponse.success(tenant, "Tenant created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN')")
    @Operation(summary = "Get tenant details by ID")
    public ResponseEntity<ApiResponse<TenantResponseDto>> getTenantById(@PathVariable UUID id) {
        TenantResponseDto tenant = tenantService.getTenantById(id);
        return ResponseEntity.ok(ApiResponse.success(tenant));
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "List all tenants with pagination")
    public ResponseEntity<ApiResponse<PageResponse<TenantResponseDto>>> getAllTenants(Pageable pageable) {
        PageResponse<TenantResponseDto> tenants = tenantService.getAllTenants(pageable);
        return ResponseEntity.ok(ApiResponse.success(tenants));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER')")
    @Operation(summary = "Update tenant details")
    public ResponseEntity<ApiResponse<TenantResponseDto>> updateTenant(
            @PathVariable UUID id,
            @Valid @RequestBody TenantRequestDto request) {
        TenantResponseDto tenant = tenantService.updateTenant(id, request);
        return ResponseEntity.ok(ApiResponse.success(tenant, "Tenant updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Delete / deactivate tenant")
    public ResponseEntity<ApiResponse<Void>> deleteTenant(@PathVariable UUID id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tenant deactivated successfully"));
    }
}
