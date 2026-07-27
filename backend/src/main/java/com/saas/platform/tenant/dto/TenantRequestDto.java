package com.saas.platform.tenant.dto;

import com.saas.platform.tenant.entity.SubscriptionPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenantRequestDto {

    @NotBlank(message = "Organization name is required")
    @Size(min = 2, max = 100)
    private String name;

    private String domain;
    private SubscriptionPlan plan;
    private String logoUrl;
    private String primaryColor;
    private Integer maxUsers;
}
