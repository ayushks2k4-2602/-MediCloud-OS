package com.saas.platform.tenant.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.tenant.dto.TenantRequestDto;
import com.saas.platform.tenant.dto.TenantResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TenantService {
    TenantResponseDto createTenant(TenantRequestDto request);
    TenantResponseDto getTenantById(UUID id);
    PageResponse<TenantResponseDto> getAllTenants(Pageable pageable);
    TenantResponseDto updateTenant(UUID id, TenantRequestDto request);
    void deleteTenant(UUID id);
}
