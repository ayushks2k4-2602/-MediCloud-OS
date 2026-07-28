package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Bed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BedRepository extends JpaRepository<Bed, UUID> {
    List<Bed> findByTenantIdAndWardId(UUID tenantId, UUID wardId);
    Page<Bed> findByTenantId(UUID tenantId, Pageable pageable);
}
