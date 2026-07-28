package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.LabOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, UUID> {
    Page<LabOrder> findByTenantId(UUID tenantId, Pageable pageable);
    Page<LabOrder> findByTenantIdAndPatientId(UUID tenantId, UUID patientId, Pageable pageable);
}
