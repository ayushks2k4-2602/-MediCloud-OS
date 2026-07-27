package com.saas.platform.hospital.controller;

import com.saas.platform.common.dto.ApiResponse;
import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.hospital.dto.TelehealthSessionDto;
import com.saas.platform.hospital.service.TelehealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hospital/telehealth")
@RequiredArgsConstructor
@Tag(name = "Telehealth & Video Consultation", description = "Online Doctor-Patient Video Call Consultation APIs")
public class TelehealthController {

    private final TelehealthService telehealthService;

    @PostMapping("/session")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR')")
    @Operation(summary = "Create an online video consultation session")
    public ResponseEntity<ApiResponse<TelehealthSessionDto>> createSession(@Valid @RequestBody TelehealthSessionDto request) {
        TelehealthSessionDto session = telehealthService.createSession(request);
        return new ResponseEntity<>(ApiResponse.success(session, "Video consultation session initialized"), HttpStatus.CREATED);
    }

    @GetMapping("/session/{roomId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR', 'PATIENT')")
    @Operation(summary = "Get room session details by Room ID")
    public ResponseEntity<ApiResponse<TelehealthSessionDto>> getSessionByRoomId(@PathVariable String roomId) {
        TelehealthSessionDto session = telehealthService.getSessionByRoomId(roomId);
        return ResponseEntity.ok(ApiResponse.success(session));
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR')")
    @Operation(summary = "List all video consultation sessions")
    public ResponseEntity<ApiResponse<PageResponse<TelehealthSessionDto>>> getSessions(Pageable pageable) {
        PageResponse<TelehealthSessionDto> sessions = telehealthService.getTenantSessions(pageable);
        return ResponseEntity.ok(ApiResponse.success(sessions));
    }
}
