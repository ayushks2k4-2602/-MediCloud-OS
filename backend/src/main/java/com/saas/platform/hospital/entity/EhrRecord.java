package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ehr_records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EhrRecord extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "appointment_id")
    private UUID appointmentId;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "diagnoses", nullable = false, columnDefinition = "TEXT")
    private String diagnoses;

    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;

    @Column(name = "vitals_json", columnDefinition = "TEXT")
    private String vitalsJson;

    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    private String doctorNotes;

    @Column(name = "soap_notes", columnDefinition = "TEXT")
    private String soapNotes;

    @Column(name = "immunizations", columnDefinition = "TEXT")
    private String immunizations;

    @Column(name = "surgery_history", columnDefinition = "TEXT")
    private String surgeryHistory;

    @Column(name = "family_history", columnDefinition = "TEXT")
    private String familyHistory;

    @Column(name = "attachments_json", columnDefinition = "TEXT")
    private String attachmentsJson;
}
