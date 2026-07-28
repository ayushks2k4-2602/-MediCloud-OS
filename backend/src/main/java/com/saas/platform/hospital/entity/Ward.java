package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "wards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ward extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 50)
    private String type; // GENERAL, ICU, PRIVATE, DELUXE, PEDIATRIC, SURGICAL

    @Column(name = "total_beds", nullable = false)
    @Builder.Default
    private Integer totalBeds = 10;

    @Column(name = "available_beds", nullable = false)
    @Builder.Default
    private Integer availableBeds = 10;
}
