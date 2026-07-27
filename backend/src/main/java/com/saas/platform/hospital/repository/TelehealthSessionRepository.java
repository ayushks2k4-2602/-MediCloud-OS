package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.TelehealthSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TelehealthSessionRepository extends JpaRepository<TelehealthSession, UUID> {
    Optional<TelehealthSession> findByRoomId(String roomId);
    Page<TelehealthSession> findByTenantId(UUID tenantId, Pageable pageable);
}
