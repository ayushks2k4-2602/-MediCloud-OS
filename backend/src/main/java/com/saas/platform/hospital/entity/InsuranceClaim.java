package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "insurance_claims")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceClaim extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "claim_number", nullable = false, length = 50)
    private String claimNumber;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "insurance_provider_id", nullable = false)
    private UUID insuranceProviderId;

    @Column(name = "invoice_id")
    private UUID invoiceId;

    @Column(name = "claim_amount", nullable = false)
    private BigDecimal claimAmount;

    @Column(name = "approved_amount")
    @Builder.Default
    private BigDecimal approvedAmount = BigDecimal.ZERO;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "SUBMITTED"; // SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, PAID

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
