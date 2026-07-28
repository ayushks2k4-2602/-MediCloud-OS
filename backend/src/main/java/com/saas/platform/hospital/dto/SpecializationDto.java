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
public class SpecializationDto {

    private UUID id;

    @NotBlank(message = "Specialization name is required")
    private String name;

    @NotBlank(message = "Specialization code is required")
    private String code;

    private String description;
}
