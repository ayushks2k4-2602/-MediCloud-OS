package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lab_orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabOrder extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "lab_test_id", nullable = false)
    private UUID labTestId;

    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING";

    @Column(name = "result_text", columnDefinition = "TEXT")
    private String resultText;

    @Column(name = "result_file_url")
    private String resultFileUrl;

    @Builder.Default
    @Column(name = "order_date", nullable = false)
    private Instant orderDate = Instant.now();

    @Column(name = "completed_date")
    private Instant completedDate;
}
