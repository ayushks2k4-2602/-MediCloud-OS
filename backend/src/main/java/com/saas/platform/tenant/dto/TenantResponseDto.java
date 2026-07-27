package com.saas.platform.tenant.dto;

import com.saas.platform.tenant.entity.SubscriptionPlan;
import com.saas.platform.tenant.entity.TenantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenantResponseDto {

    private UUID id;
    private String name;
    private String domain;
    private SubscriptionPlan plan;
    private TenantStatus status;
    private String logoUrl;
    private String primaryColor;
    private Integer maxUsers;
    private Instant createdAt;
    private Instant updatedAt;
}
