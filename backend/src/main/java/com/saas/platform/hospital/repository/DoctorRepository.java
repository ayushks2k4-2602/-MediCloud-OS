package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Page<Doctor> findByTenantId(UUID tenantId, Pageable pageable);
    
    List<Doctor> findByTenantIdAndDepartmentId(UUID tenantId, UUID departmentId);

    @Query("SELECT d FROM Doctor d WHERE d.tenantId = :tenantId AND " +
           "(:search IS NULL OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Doctor> findByTenantIdWithFilters(
            @Param("tenantId") UUID tenantId,
            @Param("search") String search,
            Pageable pageable
    );
}
