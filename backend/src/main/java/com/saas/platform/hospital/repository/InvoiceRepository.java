package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Page<Invoice> findByTenantId(UUID tenantId, Pageable pageable);
    Page<Invoice> findByTenantIdAndPatientId(UUID tenantId, UUID patientId, Pageable pageable);
}
