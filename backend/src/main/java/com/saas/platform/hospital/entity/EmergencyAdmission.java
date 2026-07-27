package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "emergency_admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAdmission extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "patient_id")
    private UUID patientId;

    @Column(name = "triage_level", nullable = false, length = 30)
    private String triageLevel; // CRITICAL, SEVERE, MODERATE, LOW

    @Column(name = "symptoms_summary", nullable = false, columnDefinition = "TEXT")
    private String symptomsSummary;

    @Column(name = "ambulance_id")
    private UUID ambulanceId;

    @Column(name = "assigned_doctor_id")
    private UUID assignedDoctorId;

    @Column(name = "admission_status", nullable = false, length = 30)
    @Builder.Default
    private String admissionStatus = "ADMITTED";

    @Builder.Default
    @Column(name = "admitted_at", nullable = false)
    private Instant admittedAt = Instant.now();
}
