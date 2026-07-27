package com.saas.platform.tenant.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.common.exception.BadRequestException;
import com.saas.platform.common.exception.ResourceNotFoundException;
import com.saas.platform.tenant.dto.TenantRequestDto;
import com.saas.platform.tenant.dto.TenantResponseDto;
import com.saas.platform.tenant.entity.SubscriptionPlan;
import com.saas.platform.tenant.entity.Tenant;
import com.saas.platform.tenant.entity.TenantStatus;
import com.saas.platform.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public TenantResponseDto createTenant(TenantRequestDto request) {
        if (request.getDomain() != null && tenantRepository.existsByDomain(request.getDomain())) {
            throw new BadRequestException("Tenant with domain '" + request.getDomain() + "' already exists");
        }

        Tenant tenant = Tenant.builder()
                .name(request.getName())
                .domain(request.getDomain())
                .plan(request.getPlan() != null ? request.getPlan() : SubscriptionPlan.FREE)
                .status(TenantStatus.ACTIVE)
                .logoUrl(request.getLogoUrl())
                .primaryColor(request.getPrimaryColor() != null ? request.getPrimaryColor() : "#4F46E5")
                .maxUsers(request.getMaxUsers() != null ? request.getMaxUsers() : 10)
                .build();

        Tenant savedTenant = tenantRepository.save(tenant);
        log.info("Tenant created: id={}, name={}", savedTenant.getId(), savedTenant.getName());
        return mapToDto(savedTenant);
    }

    @Override
    @Transactional(readOnly = true)
    public TenantResponseDto getTenantById(UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", "id", id));
        return mapToDto(tenant);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TenantResponseDto> getAllTenants(Pageable pageable) {
        Page<TenantResponseDto> page = tenantRepository.findAll(pageable).map(this::mapToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public TenantResponseDto updateTenant(UUID id, TenantRequestDto request) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", "id", id));

        if (request.getName() != null) tenant.setName(request.getName());
        if (request.getLogoUrl() != null) tenant.setLogoUrl(request.getLogoUrl());
        if (request.getPrimaryColor() != null) tenant.setPrimaryColor(request.getPrimaryColor());
        if (request.getPlan() != null) tenant.setPlan(request.getPlan());
        if (request.getMaxUsers() != null) tenant.setMaxUsers(request.getMaxUsers());

        Tenant updated = tenantRepository.save(tenant);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void deleteTenant(UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", "id", id));
        tenant.setStatus(TenantStatus.CANCELLED);
        tenantRepository.save(tenant);
    }

    private TenantResponseDto mapToDto(Tenant tenant) {
        return TenantResponseDto.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .domain(tenant.getDomain())
                .plan(tenant.getPlan())
                .status(tenant.getStatus())
                .logoUrl(tenant.getLogoUrl())
                .primaryColor(tenant.getPrimaryColor())
                .maxUsers(tenant.getMaxUsers())
                .createdAt(tenant.getCreatedAt())
                .updatedAt(tenant.getUpdatedAt())
                .build();
    }
}
