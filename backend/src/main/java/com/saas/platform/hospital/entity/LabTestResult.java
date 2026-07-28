package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "lab_test_results")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTestResult extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "lab_order_id", nullable = false)
    private UUID labOrderId;

    @Column(name = "test_catalog_id", nullable = false)
    private UUID testCatalogId;

    @Column(name = "result_value", nullable = false)
    private String resultValue;

    @Column(name = "normal_range", length = 100)
    private String normalRange;

    @Column(name = "unit", length = 30)
    private String unit;

    @Column(name = "is_critical")
    @Builder.Default
    private Boolean isCritical = false;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "PENDING_APPROVAL"; // PENDING_APPROVAL, APPROVED, REJECTED

    @Column(name = "pathologist_notes", columnDefinition = "TEXT")
    private String pathologistNotes;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;
}
