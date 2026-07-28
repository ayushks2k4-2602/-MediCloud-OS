package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.LabTestCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabTestCatalogRepository extends JpaRepository<LabTestCatalog, UUID> {
    List<LabTestCatalog> findByTenantId(UUID tenantId);
    Page<LabTestCatalog> findByTenantId(UUID tenantId, Pageable pageable);
}
