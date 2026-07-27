package com.saas.platform.hospital.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.common.exception.ResourceNotFoundException;
import com.saas.platform.hospital.dto.TelehealthSessionDto;
import com.saas.platform.hospital.entity.TelehealthSession;
import com.saas.platform.hospital.repository.TelehealthSessionRepository;
import com.saas.platform.tenant.context.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelehealthServiceImpl implements TelehealthService {

    private final TelehealthSessionRepository telehealthSessionRepository;

    @Override
    @Transactional
    public TelehealthSessionDto createSession(TelehealthSessionDto request) {
        UUID tenantId = TenantContext.getTenantId();
        String roomId = "ROOM-" + UUID.randomUUID().toString().substring(0, 8);
        String joinToken = "JWT-ROOM-" + UUID.randomUUID();

        TelehealthSession session = TelehealthSession.builder()
                .tenantId(tenantId)
                .appointmentId(request.getAppointmentId())
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .roomId(roomId)
                .joinToken(joinToken)
                .status("CREATED")
                .scheduledStart(request.getScheduledStart())
                .build();

        TelehealthSession saved = telehealthSessionRepository.save(session);
        log.info("Telehealth video session created: roomId={}", roomId);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TelehealthSessionDto getSessionByRoomId(String roomId) {
        TelehealthSession session = telehealthSessionRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("TelehealthSession", "roomId", roomId));
        return mapToDto(session);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TelehealthSessionDto> getTenantSessions(Pageable pageable) {
        UUID tenantId = TenantContext.getTenantId();
        Page<TelehealthSessionDto> page = telehealthSessionRepository.findByTenantId(tenantId, pageable).map(this::mapToDto);
        return PageResponse.from(page);
    }

    private TelehealthSessionDto mapToDto(TelehealthSession s) {
        return TelehealthSessionDto.builder()
                .id(s.getId())
                .appointmentId(s.getAppointmentId())
                .doctorId(s.getDoctorId())
                .patientId(s.getPatientId())
                .roomId(s.getRoomId())
                .joinToken(s.getJoinToken())
                .status(s.getStatus())
                .scheduledStart(s.getScheduledStart())
                .actualEnd(s.getActualEnd())
                .build();
    }
}
