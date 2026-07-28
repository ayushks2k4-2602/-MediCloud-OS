package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "stock_movements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "medicine_id", nullable = false)
    private UUID medicineId;

    @Column(name = "movement_type", nullable = false, length = 30)
    private String movementType; // PURCHASE, DISPENSE, ADJUSTMENT, RETURN

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reason", length = 255)
    private String reason;
}
