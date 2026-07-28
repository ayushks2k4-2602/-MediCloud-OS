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
@Table(name = "prescription_fulfillments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionFulfillment extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "fulfillment_number", nullable = false, length = 50)
    private String fulfillmentNumber;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "medicine_id", nullable = false)
    private UUID medicineId;

    @Column(name = "quantity_dispensed", nullable = false)
    @Builder.Default
    private Integer quantityDispensed = 1;

    @Column(name = "unit_price", nullable = false)
    @Builder.Default
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "total_price", nullable = false)
    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "DISPENSED";

    @Column(name = "dispensed_at")
    @Builder.Default
    private ZonedDateTime dispensedAt = ZonedDateTime.now();
}
