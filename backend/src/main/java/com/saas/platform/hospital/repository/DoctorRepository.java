package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Page<Doctor> findByTenantId(UUID tenantId, Pageable pageable);
    List<Doctor> findByTenantIdAndDepartmentId(UUID tenantId, UUID departmentId);
}
