package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.AppointmentWaitingList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppointmentWaitingListRepository extends JpaRepository<AppointmentWaitingList, UUID> {
    Page<AppointmentWaitingList> findByTenantId(UUID tenantId, Pageable pageable);
}
