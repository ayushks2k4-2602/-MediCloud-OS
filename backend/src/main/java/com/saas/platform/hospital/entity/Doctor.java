package com.saas.platform.hospital.entity;

import com.saas.platform.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "specialization_id")
    private UUID specializationId;

    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;

    @Column(name = "qualification", length = 100)
    private String qualification;

    @Column(name = "experience_years")
    @Builder.Default
    private Integer experienceYears = 5;

    @Column(name = "contact_number", length = 30)
    private String contactNumber;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @Column(name = "employment_status", length = 30)
    @Builder.Default
    private String employmentStatus = "FULL_TIME";

    @Column(name = "consultation_fee", nullable = false)
    @Builder.Default
    private BigDecimal consultationFee = new BigDecimal("50.00");

    @Column(name = "license_number", nullable = false, length = 50)
    private String licenseNumber;

    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;
}
