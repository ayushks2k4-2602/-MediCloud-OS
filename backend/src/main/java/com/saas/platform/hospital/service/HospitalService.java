package com.saas.platform.hospital.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.hospital.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface HospitalService {
    PatientDto registerPatient(PatientDto request);
    PatientDto getPatientById(UUID id);
    PageResponse<PatientDto> getTenantPatients(String search, String bloodGroup, Pageable pageable);
    
    DoctorDto addDoctor(DoctorDto request);
    DoctorDto updateDoctor(UUID id, DoctorDto request);
    void deleteDoctor(UUID id);
    PageResponse<DoctorDto> getTenantDoctors(String search, Pageable pageable);

    SpecializationDto createSpecialization(SpecializationDto request);
    List<SpecializationDto> getSpecializations();

    ShiftDto createShift(ShiftDto request);
    List<ShiftDto> getShifts();

    AppointmentDto scheduleAppointment(AppointmentDto request);
    AppointmentDto updateAppointmentStatus(UUID appointmentId, String status);
    PageResponse<AppointmentDto> getTenantAppointments(Pageable pageable);

    EhrRecordDto saveEhrRecord(EhrRecordDto request);
    PageResponse<EhrRecordDto> getPatientEhrRecords(UUID patientId, Pageable pageable);

    MedicineDto addMedicine(MedicineDto request);
    PageResponse<MedicineDto> getMedicines(Pageable pageable);
}
