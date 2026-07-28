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
public class LabTestResultDto {

    private UUID id;

    @NotNull(message = "Lab order ID is required")
    private UUID labOrderId;

    @NotNull(message = "Test catalog ID is required")
    private UUID testCatalogId;

    @NotBlank(message = "Result value is required")
    private String resultValue;

    private String normalRange;
    private String unit;
    private Boolean isCritical;
    private String status;
    private String pathologistNotes;
    private ZonedDateTime approvedAt;
}
