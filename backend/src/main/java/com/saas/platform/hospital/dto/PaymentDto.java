package com.saas.platform.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private UUID id;

    @NotNull(message = "Invoice ID is required")
    private UUID invoiceId;

    private String paymentNumber;

    @NotNull(message = "Payment amount is required")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // CASH, CREDIT_CARD, STRIPE, INSURANCE, BANK_TRANSFER

    private String transactionReference;
    private String status;
    private ZonedDateTime paymentDate;
}
