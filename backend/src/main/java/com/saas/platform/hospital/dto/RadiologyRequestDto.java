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
public class RadiologyRequestDto {

    private UUID id;
    private String requestNumber;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private UUID doctorId;

    @NotBlank(message = "Modality is required")
    private String modality; // X_RAY, CT_SCAN, MRI, ULTRASOUND, PET_SCAN

    @NotBlank(message = "Body part is required")
    private String bodyPart;

    private String status;
    private String imageUrl;
    private String radiologistReport;
}
