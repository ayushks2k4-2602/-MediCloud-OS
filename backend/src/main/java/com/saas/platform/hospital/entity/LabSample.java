package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "lab_samples")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabSample extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "sample_code", nullable = false, length = 50)
    private String sampleCode;

    @Column(name = "lab_order_id", nullable = false)
    private UUID labOrderId;

    @Column(name = "specimen_type", nullable = false, length = 50)
    private String specimenType;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "COLLECTED"; // COLLECTED, RECEIVED, IN_TESTING, REJECTED

    @Column(name = "collected_at")
    @Builder.Default
    private ZonedDateTime collectedAt = ZonedDateTime.now();

    @Column(name = "received_at")
    private ZonedDateTime receivedAt;
}
