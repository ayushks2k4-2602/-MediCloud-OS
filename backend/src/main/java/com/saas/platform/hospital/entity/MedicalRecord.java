package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "appointment_id")
    private UUID appointmentId;

    @Column(name = "symptoms", columnDefinition = "TEXT")
    private String symptoms;

    @Column(name = "diagnosis", nullable = false, columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "vital_bp", length = 20)
    private String vitalBp;

    @Column(name = "vital_heart_rate")
    private Integer vitalHeartRate;

    @Column(name = "vital_temp", precision = 4, scale = 1)
    private BigDecimal vitalTemp;

    @Column(name = "vital_weight", precision = 5, scale = 2)
    private BigDecimal vitalWeight;

    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    private String doctorNotes;
}
