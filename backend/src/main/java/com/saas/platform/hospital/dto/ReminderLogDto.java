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
public class ReminderLogDto {

    private UUID id;
    private UUID appointmentId;
    private String channel;
    private String recipient;
    private String message;
    private String status;
    private ZonedDateTime sentAt;
}
