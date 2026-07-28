package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.PharmacySupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PharmacySupplierRepository extends JpaRepository<PharmacySupplier, UUID> {
    List<PharmacySupplier> findByTenantId(UUID tenantId);
    Page<PharmacySupplier> findByTenantId(UUID tenantId, Pageable pageable);
}
