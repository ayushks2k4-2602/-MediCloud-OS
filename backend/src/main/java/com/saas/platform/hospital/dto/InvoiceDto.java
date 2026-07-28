package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private UUID id;
    private String invoiceNumber;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private UUID appointmentId;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate dueDate;
    private List<InvoiceItemDto> items;
}
