package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "reminder_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderLog extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "appointment_id")
    private UUID appointmentId;

    @Column(name = "channel", nullable = false, length = 20)
    private String channel;

    @Column(name = "recipient", nullable = false, length = 100)
    private String recipient;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "SENT";

    @Column(name = "sent_at")
    @Builder.Default
    private ZonedDateTime sentAt = ZonedDateTime.now();
}
