package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "invoice_id", nullable = false)
    private UUID invoiceId;

    @Column(name = "payment_number", nullable = false, length = 50)
    private String paymentNumber;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod; // CASH, CREDIT_CARD, STRIPE, INSURANCE, BANK_TRANSFER

    @Column(name = "transaction_reference", length = 100)
    private String transactionReference;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "COMPLETED"; // PENDING, COMPLETED, FAILED, REFUNDED

    @Column(name = "payment_date")
    @Builder.Default
    private ZonedDateTime paymentDate = ZonedDateTime.now();
}
