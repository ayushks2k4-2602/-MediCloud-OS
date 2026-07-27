package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelehealthSessionDto {

    private UUID id;
    private UUID appointmentId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private String roomId;
    private String joinToken;
    private String status;

    @NotNull(message = "Scheduled start time is required")
    private Instant scheduledStart;

    private Instant actualEnd;
}
