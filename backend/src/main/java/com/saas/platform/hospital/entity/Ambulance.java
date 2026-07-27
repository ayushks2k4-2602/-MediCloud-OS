package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ambulances")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ambulance extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "vehicle_number", nullable = false, length = 30)
    private String vehicleNumber;

    @Column(name = "driver_name", nullable = false, length = 100)
    private String driverName;

    @Column(name = "driver_phone", nullable = false, length = 30)
    private String driverPhone;

    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private String status = "AVAILABLE";

    @Column(name = "current_location", length = 150)
    private String currentLocation;
}
