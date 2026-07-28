package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
    Page<StockMovement> findByTenantId(UUID tenantId, Pageable pageable);
    List<StockMovement> findByTenantIdAndMedicineId(UUID tenantId, UUID medicineId);
}
