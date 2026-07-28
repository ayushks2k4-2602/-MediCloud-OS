package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.AiClinicalCopilot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AiClinicalCopilotRepository extends JpaRepository<AiClinicalCopilot, UUID> {
    Page<AiClinicalCopilot> findByTenantId(UUID tenantId, Pageable pageable);
    Page<AiClinicalCopilot> findByTenantIdAndPatientId(UUID tenantId, UUID patientId, Pageable pageable);
}
