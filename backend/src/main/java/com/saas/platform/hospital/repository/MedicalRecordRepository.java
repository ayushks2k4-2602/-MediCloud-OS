package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {
    Page<MedicalRecord> findByTenantIdAndPatientId(UUID tenantId, UUID patientId, Pageable pageable);
}
