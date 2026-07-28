package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "appointment_waiting_list")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentWaitingList extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "requested_date", nullable = false)
    private LocalDate requestedDate;

    @Column(name = "preferred_time_slot", length = 30)
    private String preferredTimeSlot;

    @Column(name = "priority_notes", columnDefinition = "TEXT")
    private String priorityNotes;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "WAITING";
}
