package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.LabTestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabTestResultRepository extends JpaRepository<LabTestResult, UUID> {
    Page<LabTestResult> findByTenantId(UUID tenantId, Pageable pageable);
    List<LabTestResult> findByTenantIdAndLabOrderId(UUID tenantId, UUID labOrderId);
}
