package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "radiology_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RadiologyRequest extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "request_number", nullable = false, length = 50)
    private String requestNumber;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "modality", nullable = false, length = 50)
    private String modality; // X_RAY, CT_SCAN, MRI, ULTRASOUND, PET_SCAN

    @Column(name = "body_part", nullable = false, length = 100)
    private String bodyPart;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "REQUESTED"; // REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "radiologist_report", columnDefinition = "TEXT")
    private String radiologistReport;
}
