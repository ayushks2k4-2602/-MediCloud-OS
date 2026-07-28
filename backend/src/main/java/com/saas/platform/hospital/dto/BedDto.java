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
public class BedDto {

    private UUID id;

    @NotNull(message = "Ward ID is required")
    private UUID wardId;

    @NotBlank(message = "Bed number is required")
    private String bedNumber;

    private String bedType;
    private String status;
    private UUID patientId;
    private BigDecimal dailyCharge;
}
