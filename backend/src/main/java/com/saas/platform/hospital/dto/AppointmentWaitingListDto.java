package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentWaitingListDto {

    private UUID id;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "Requested date is required")
    private LocalDate requestedDate;

    private String preferredTimeSlot;
    private String priorityNotes;
    private String status;
}
