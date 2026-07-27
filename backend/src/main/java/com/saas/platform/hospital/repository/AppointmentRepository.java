package com.saas.platform.hospital.repository;

import com.saas.platform.hospital.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Page<Appointment> findByTenantId(UUID tenantId, Pageable pageable);
    List<Appointment> findByTenantIdAndDoctorIdAndAppointmentDate(UUID tenantId, UUID doctorId, LocalDate appointmentDate);
    Page<Appointment> findByTenantIdAndPatientId(UUID tenantId, UUID patientId, Pageable pageable);
}
