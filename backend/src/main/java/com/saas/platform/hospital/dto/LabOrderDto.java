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
public class LabOrderDto {

    private UUID id;
    private String orderNumber;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private UUID doctorId;
    private UUID appointmentId;
    private String status;
    private BigDecimal totalAmount;
}
