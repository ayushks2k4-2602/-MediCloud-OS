package com.saas.platform.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDto {

    private UUID id;
    private UUID userId;
    private String action;
    private String resource;
    private String details;
    private String ipAddress;
    private ZonedDateTime timestamp;
}
