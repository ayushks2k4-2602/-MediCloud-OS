package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Page<Patient> findByTenantId(UUID tenantId, Pageable pageable);
    
    Optional<Patient> findByTenantIdAndPatientCode(UUID tenantId, String patientCode);

    @Query("SELECT p FROM Patient p WHERE p.tenantId = :tenantId AND " +
           "(:search IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.patientCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.phone) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:bloodGroup IS NULL OR p.bloodGroup = :bloodGroup)")
    Page<Patient> findByTenantIdWithFilters(
            @Param("tenantId") UUID tenantId,
            @Param("search") String search,
            @Param("bloodGroup") String bloodGroup,
            Pageable pageable
    );
}
