package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Page<Payment> findByTenantId(UUID tenantId, Pageable pageable);
    List<Payment> findByTenantIdAndInvoiceId(UUID tenantId, UUID invoiceId);
}
