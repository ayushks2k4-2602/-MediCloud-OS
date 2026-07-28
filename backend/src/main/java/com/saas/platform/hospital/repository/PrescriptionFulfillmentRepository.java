package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.PrescriptionFulfillment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrescriptionFulfillmentRepository extends JpaRepository<PrescriptionFulfillment, UUID> {
    Page<PrescriptionFulfillment> findByTenantId(UUID tenantId, Pageable pageable);
    Page<PrescriptionFulfillment> findByTenantIdAndPatientId(UUID tenantId, UUID patientId, Pageable pageable);
}
