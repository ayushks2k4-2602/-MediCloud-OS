package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shift extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "start_time", nullable = false, length = 20)
    private String startTime;

    @Column(name = "end_time", nullable = false, length = 20)
    private String endTime;

    @Column(name = "working_days", length = 100)
    @Builder.Default
    private String workingDays = "Mon,Tue,Wed,Thu,Fri";

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "ACTIVE";
}
