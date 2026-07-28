package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findByTenantId(UUID tenantId);
    Page<Shift> findByTenantId(UUID tenantId, Pageable pageable);
}
