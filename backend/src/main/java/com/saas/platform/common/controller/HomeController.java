package com.saas.platform.common.controller;

import com.saas.platform.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "Backend API Welcome Endpoint")
    public ResponseEntity<ApiResponse<Map<String, Object>>> home() {
        Map<String, Object> details = Map.of(
                "application", "MediCloud OS - Multi-Tenant Hospital Management SaaS",
                "status", "UP",
                "version", "1.0.0",
                "swaggerUi", "/swagger-ui.html",
                "apiDocs", "/v3/api-docs"
        );
        return ResponseEntity.ok(ApiResponse.success(details, "MediCloud OS REST API Engine is running"));
    }
}
