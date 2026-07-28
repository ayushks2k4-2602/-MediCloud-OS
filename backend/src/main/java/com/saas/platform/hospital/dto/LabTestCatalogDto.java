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
public class LabTestCatalogDto {

    private UUID id;

    @NotBlank(message = "Test name is required")
    private String name;

    @NotBlank(message = "Test code is required")
    private String code;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotBlank(message = "Sample type is required")
    private String sampleType;

    private String normalRange;
    private String unit;
}
