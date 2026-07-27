package com.saas.platform.user.dto;

import com.saas.platform.user.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private UUID id;
    private UUID tenantId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String avatarUrl;
    private UserStatus status;
    private Boolean emailVerified;
    private Boolean twoFactorEnabled;
    private Set<String> roles;
    private Instant createdAt;
    private Instant updatedAt;
}
