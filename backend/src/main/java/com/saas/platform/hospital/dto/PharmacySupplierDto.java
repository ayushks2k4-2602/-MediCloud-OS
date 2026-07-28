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
public class PharmacySupplierDto {

    private UUID id;

    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Supplier code is required")
    private String code;

    private String contactPhone;
    private String email;
    private String address;
}
