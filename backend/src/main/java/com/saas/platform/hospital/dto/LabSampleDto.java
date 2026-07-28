package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabSampleDto {

    private UUID id;
    private String sampleCode;

    @NotNull(message = "Lab order ID is required")
    private UUID labOrderId;

    @NotBlank(message = "Specimen type is required")
    private String specimenType;

    private String status;
    private ZonedDateTime collectedAt;
    private ZonedDateTime receivedAt;
}
