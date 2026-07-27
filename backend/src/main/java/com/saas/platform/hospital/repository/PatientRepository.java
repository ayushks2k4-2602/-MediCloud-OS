package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Page<Patient> findByTenantId(UUID tenantId, Pageable pageable);
    Optional<Patient> findByTenantIdAndPatientCode(UUID tenantId, String patientCode);
}
