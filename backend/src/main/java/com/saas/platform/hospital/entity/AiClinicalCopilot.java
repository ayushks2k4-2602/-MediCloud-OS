package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ai_clinical_copilots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiClinicalCopilot extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "summary_type", nullable = false, length = 50)
    private String summaryType; // SOAP_SUMMARY, VISIT_SUMMARY, DISCHARGE_DRAFT, DIAGNOSTIC_ADVISORY

    @Column(name = "ai_generated_notes", nullable = false, columnDefinition = "TEXT")
    private String aiGeneratedNotes;

    @Column(name = "is_reviewed_by_human")
    @Builder.Default
    private Boolean isReviewedByHuman = false;
}
