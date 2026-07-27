package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    Page<Medicine> findByTenantId(UUID tenantId, Pageable pageable);
}
