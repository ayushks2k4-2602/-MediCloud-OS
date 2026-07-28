package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.InsuranceProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsuranceProviderRepository extends JpaRepository<InsuranceProvider, UUID> {
    List<InsuranceProvider> findByTenantId(UUID tenantId);
    Page<InsuranceProvider> findByTenantId(UUID tenantId, Pageable pageable);
}
