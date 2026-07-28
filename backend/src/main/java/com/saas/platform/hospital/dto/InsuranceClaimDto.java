package com.saas.platform.hospital.dto;

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
public class InsuranceClaimDto {

    private UUID id;
    private String claimNumber;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Insurance provider ID is required")
    private UUID insuranceProviderId;

    private UUID invoiceId;

    @NotNull(message = "Claim amount is required")
    private BigDecimal claimAmount;

    private BigDecimal approvedAmount;
    private String status;
    private String notes;
}
