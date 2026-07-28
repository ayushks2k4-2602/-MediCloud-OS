package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, UUID> {
    List<Specialization> findByTenantId(UUID tenantId);
    Page<Specialization> findByTenantId(UUID tenantId, Pageable pageable);
}
