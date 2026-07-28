package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.LabSample;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabSampleRepository extends JpaRepository<LabSample, UUID> {
    Page<LabSample> findByTenantId(UUID tenantId, Pageable pageable);
    List<LabSample> findByTenantIdAndLabOrderId(UUID tenantId, UUID labOrderId);
}
