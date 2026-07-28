package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "time_slot", nullable = false, length = 30)
    private String timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AppointmentStatus status;

    @Column(name = "type", length = 30)
    private String type;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "rescheduled_from_id")
    private UUID rescheduledFromId;

    @Column(name = "reminder_sent_email")
    @Builder.Default
    private Boolean reminderSentEmail = false;

    @Column(name = "reminder_sent_sms")
    @Builder.Default
    private Boolean reminderSentSms = false;
}
