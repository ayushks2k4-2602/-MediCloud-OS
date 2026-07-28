package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WardDto {

    private UUID id;

    @NotBlank(message = "Ward name is required")
    private String name;

    @NotBlank(message = "Ward type is required")
    private String type; // GENERAL, ICU, PRIVATE, DELUXE, PEDIATRIC, SURGICAL

    private Integer totalBeds;
    private Integer availableBeds;
}
