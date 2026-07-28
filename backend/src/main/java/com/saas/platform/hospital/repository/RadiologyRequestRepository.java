package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.RadiologyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RadiologyRequestRepository extends JpaRepository<RadiologyRequest, UUID> {
    Page<RadiologyRequest> findByTenantId(UUID tenantId, Pageable pageable);
    Page<RadiologyRequest> findByTenantIdAndPatientId(UUID tenantId, UUID patientId, Pageable pageable);
}
