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
public class EhrRecordDto {

    private UUID id;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private UUID doctorId;
    private UUID appointmentId;

    private String medicalHistory;

    @NotBlank(message = "Diagnoses is required")
    private String diagnoses;

    private String allergies;
    private String vitalsJson;
    private String doctorNotes;
    private String soapNotes;
    private String immunizations;
    private String surgeryHistory;
    private String familyHistory;
    private String attachmentsJson;
}
