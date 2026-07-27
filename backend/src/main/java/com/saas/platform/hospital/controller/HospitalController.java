package com.saas.platform.hospital.controller;

import com.saas.platform.common.dto.ApiResponse;
import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.hospital.dto.AppointmentDto;
import com.saas.platform.hospital.dto.MedicineDto;
import com.saas.platform.hospital.dto.PatientDto;
import com.saas.platform.hospital.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
@Tag(name = "Hospital Management", description = "Multi-Tenant Hospital Management APIs (Patients, Appointments, EMR, Pharmacy)")
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping("/patients")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    @Operation(summary = "Register a new patient")
    public ResponseEntity<ApiResponse<PatientDto>> registerPatient(@Valid @RequestBody PatientDto request) {
        PatientDto patient = hospitalService.registerPatient(request);
        return new ResponseEntity<>(ApiResponse.success(patient, "Patient registered successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/patients/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse<PatientDto>> getPatientById(@PathVariable UUID id) {
        PatientDto patient = hospitalService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.success(patient));
    }

    @GetMapping("/patients")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    @Operation(summary = "List tenant patients")
    public ResponseEntity<ApiResponse<PageResponse<PatientDto>>> getPatients(Pageable pageable) {
        PageResponse<PatientDto> patients = hospitalService.getTenantPatients(pageable);
        return ResponseEntity.ok(ApiResponse.success(patients));
    }

    @PostMapping("/appointments")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT')")
    @Operation(summary = "Schedule a doctor appointment")
    public ResponseEntity<ApiResponse<AppointmentDto>> scheduleAppointment(@Valid @RequestBody AppointmentDto request) {
        AppointmentDto appointment = hospitalService.scheduleAppointment(request);
        return new ResponseEntity<>(ApiResponse.success(appointment, "Appointment scheduled"), HttpStatus.CREATED);
    }

    @GetMapping("/appointments")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    @Operation(summary = "List tenant appointments")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentDto>>> getAppointments(Pageable pageable) {
        PageResponse<AppointmentDto> appointments = hospitalService.getTenantAppointments(pageable);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @PostMapping("/medicines")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'PHARMACIST')")
    @Operation(summary = "Add new medicine to inventory")
    public ResponseEntity<ApiResponse<MedicineDto>> addMedicine(@Valid @RequestBody MedicineDto request) {
        MedicineDto medicine = hospitalService.addMedicine(request);
        return new ResponseEntity<>(ApiResponse.success(medicine, "Medicine added to inventory"), HttpStatus.CREATED);
    }

    @GetMapping("/medicines")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_OWNER', 'ADMIN', 'DOCTOR', 'PHARMACIST')")
    @Operation(summary = "List pharmacy medicine inventory")
    public ResponseEntity<ApiResponse<PageResponse<MedicineDto>>> getMedicines(Pageable pageable) {
        PageResponse<MedicineDto> medicines = hospitalService.getMedicines(pageable);
        return ResponseEntity.ok(ApiResponse.success(medicines));
    }
}
