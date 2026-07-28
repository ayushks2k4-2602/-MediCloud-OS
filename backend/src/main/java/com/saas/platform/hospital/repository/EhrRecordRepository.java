package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.EhrRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EhrRecordRepository extends JpaRepository<EhrRecord, UUID> {
    Page<EhrRecord> findByTenantIdAndPatientId(UUID tenantId, UUID patientId, Pageable pageable);
}
