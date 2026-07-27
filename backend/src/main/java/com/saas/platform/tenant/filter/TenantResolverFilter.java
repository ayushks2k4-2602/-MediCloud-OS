package com.saas.platform.tenant.filter;

import com.saas.platform.tenant.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(1)
public class TenantResolverFilter extends OncePerRequestFilter {

    @Value("${app.tenant.header-name:X-Tenant-ID}")
    private String tenantHeaderName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tenantHeader = request.getHeader(tenantHeaderName);

            if (tenantHeader != null && !tenantHeader.isBlank()) {
                try {
                    UUID tenantId = UUID.fromString(tenantHeader);
                    TenantContext.setTenantId(tenantId);
                } catch (IllegalArgumentException e) {
                    TenantContext.setTenantId(TenantContext.DEFAULT_TENANT_ID);
                }
            } else {
                TenantContext.setTenantId(TenantContext.DEFAULT_TENANT_ID);
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
