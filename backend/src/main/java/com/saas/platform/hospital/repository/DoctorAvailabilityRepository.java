package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, UUID> {
    List<DoctorAvailability> findByTenantIdAndDoctorId(UUID tenantId, UUID doctorId);
}
