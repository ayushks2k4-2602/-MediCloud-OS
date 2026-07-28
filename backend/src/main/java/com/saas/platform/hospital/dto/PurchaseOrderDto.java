package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDto {

    private UUID id;
    private String poNumber;

    @NotNull(message = "Supplier ID is required")
    private UUID supplierId;

    private BigDecimal totalAmount;
    private String status;
    private LocalDate orderDate;
}
