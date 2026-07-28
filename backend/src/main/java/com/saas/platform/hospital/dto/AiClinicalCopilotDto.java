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
public class AiClinicalCopilotDto {

    private UUID id;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private UUID doctorId;

    @NotBlank(message = "Summary type is required")
    private String summaryType; // SOAP_SUMMARY, VISIT_SUMMARY, DISCHARGE_DRAFT, DIAGNOSTIC_ADVISORY

    @NotBlank(message = "AI notes content is required")
    private String aiGeneratedNotes;

    private Boolean isReviewedByHuman;
}
