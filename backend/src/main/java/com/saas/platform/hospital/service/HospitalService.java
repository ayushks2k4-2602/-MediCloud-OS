package com.saas.platform.hospital.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.hospital.dto.AppointmentDto;
import com.saas.platform.hospital.dto.MedicineDto;
import com.saas.platform.hospital.dto.PatientDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HospitalService {
    PatientDto registerPatient(PatientDto request);
    PatientDto getPatientById(UUID id);
    PageResponse<PatientDto> getTenantPatients(Pageable pageable);
    
    AppointmentDto scheduleAppointment(AppointmentDto request);
    AppointmentDto updateAppointmentStatus(UUID appointmentId, String status);
    PageResponse<AppointmentDto> getTenantAppointments(Pageable pageable);

    MedicineDto addMedicine(MedicineDto request);
    PageResponse<MedicineDto> getMedicines(Pageable pageable);
}
