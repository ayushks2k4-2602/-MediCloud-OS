package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDto {

    private UUID id;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    private UUID appointmentId;
    private String symptoms;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    private String vitalBp;
    private Integer vitalHeartRate;
    private BigDecimal vitalTemp;
    private BigDecimal vitalWeight;
    private String doctorNotes;
}
