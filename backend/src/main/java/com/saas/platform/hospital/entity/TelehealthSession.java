package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "telehealth_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelehealthSession extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "appointment_id")
    private UUID appointmentId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "room_id", nullable = false, unique = true, length = 100)
    private String roomId;

    @Column(name = "join_token", nullable = false)
    private String joinToken;

    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private String status = "CREATED";

    @Column(name = "scheduled_start", nullable = false)
    private Instant scheduledStart;

    @Column(name = "actual_end")
    private Instant actualEnd;
}
