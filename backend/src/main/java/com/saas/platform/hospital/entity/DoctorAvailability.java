package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "doctor_availabilities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailability extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "day_of_week", nullable = false, length = 20)
    private String dayOfWeek;

    @Column(name = "start_time", nullable = false, length = 20)
    private String startTime;

    @Column(name = "end_time", nullable = false, length = 20)
    private String endTime;

    @Column(name = "slot_duration_minutes")
    @Builder.Default
    private Integer slotDurationMinutes = 30;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
