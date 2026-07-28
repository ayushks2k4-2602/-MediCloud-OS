package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionFulfillmentDto {

    private UUID id;
    private String fulfillmentNumber;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private UUID doctorId;

    @NotNull(message = "Medicine ID is required")
    private UUID medicineId;

    @NotNull(message = "Quantity is required")
    private Integer quantityDispensed;

    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String status;
    private ZonedDateTime dispensedAt;
}
