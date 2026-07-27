package com.saas.platform.hospital.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.hospital.dto.TelehealthSessionDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TelehealthService {
    TelehealthSessionDto createSession(TelehealthSessionDto request);
    TelehealthSessionDto getSessionByRoomId(String roomId);
    PageResponse<TelehealthSessionDto> getTenantSessions(Pageable pageable);
}
