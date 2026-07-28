package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAvailabilityDto {

    private UUID id;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotBlank(message = "Day of week is required")
    private String dayOfWeek;

    @NotBlank(message = "Start time is required")
    private String startTime;

    @NotBlank(message = "End time is required")
    private String endTime;

    private Integer slotDurationMinutes;
    private Boolean isActive;
}
