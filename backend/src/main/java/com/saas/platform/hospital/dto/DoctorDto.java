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
public class DoctorDto {

    private UUID id;
    private UUID userId;
    private UUID departmentId;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private String qualification;

    @NotNull(message = "Consultation fee is required")
    private BigDecimal consultationFee;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @Builder.Default
    private Boolean isAvailable = true;
}
