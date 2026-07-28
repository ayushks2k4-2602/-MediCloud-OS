package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementDto {

    private UUID id;

    @NotNull(message = "Medicine ID is required")
    private UUID medicineId;

    @NotBlank(message = "Movement type is required")
    private String movementType; // PURCHASE, DISPENSE, ADJUSTMENT, RETURN

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String reason;
}
