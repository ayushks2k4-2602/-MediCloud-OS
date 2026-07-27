package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotBlank;
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
public class MedicineDto {

    private UUID id;

    @NotBlank(message = "Medicine name is required")
    private String name;

    private String genericName;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Batch number is required")
    private String batchNumber;

    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;

    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    private String manufacturer;
}
