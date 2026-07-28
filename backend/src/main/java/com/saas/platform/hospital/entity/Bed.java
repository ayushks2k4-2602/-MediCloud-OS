package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "beds")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bed extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "ward_id", nullable = false)
    private UUID wardId;

    @Column(name = "bed_number", nullable = false, length = 30)
    private String bedNumber;

    @Column(name = "bed_type", length = 50)
    @Builder.Default
    private String bedType = "STANDARD";

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "AVAILABLE"; // AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED

    @Column(name = "patient_id")
    private UUID patientId;

    @Column(name = "daily_charge")
    @Builder.Default
    private BigDecimal dailyCharge = new BigDecimal("100.00");
}
