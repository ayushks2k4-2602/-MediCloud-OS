package com.saas.platform.hospital.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.hospital.dto.AppointmentDto;
import com.saas.platform.hospital.dto.DoctorDto;
import com.saas.platform.hospital.dto.MedicalRecordDto;
import com.saas.platform.hospital.dto.MedicineDto;
import com.saas.platform.hospital.dto.PatientDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HospitalService {
    PatientDto registerPatient(PatientDto request);
    PatientDto getPatientById(UUID id);
    PageResponse<PatientDto> getTenantPatients(String search, String bloodGroup, Pageable pageable);
    
    DoctorDto addDoctor(DoctorDto request);
    DoctorDto updateDoctor(UUID id, DoctorDto request);
    void deleteDoctor(UUID id);
    PageResponse<DoctorDto> getTenantDoctors(String search, Pageable pageable);

    AppointmentDto scheduleAppointment(AppointmentDto request);
    AppointmentDto updateAppointmentStatus(UUID appointmentId, String status);
    PageResponse<AppointmentDto> getTenantAppointments(Pageable pageable);

    MedicalRecordDto createMedicalRecord(MedicalRecordDto request);
    PageResponse<MedicalRecordDto> getPatientMedicalRecords(UUID patientId, Pageable pageable);

    MedicineDto addMedicine(MedicineDto request);
    PageResponse<MedicineDto> getMedicines(Pageable pageable);
}
