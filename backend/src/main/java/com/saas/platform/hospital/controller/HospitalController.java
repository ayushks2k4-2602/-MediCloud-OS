package com.saas.platform.hospital.controller;

import com.saas.platform.common.dto.ApiResponse;
import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.hospital.dto.*;
import com.saas.platform.hospital.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
@Tag(name = "Hospital Management", description = "Multi-Tenant Hospital Management APIs (Patients, Doctors, Appointments, EMR, Pharmacy)")
public class HospitalController {

    private final HospitalService hospitalService;

    // PATIENTS
    @PostMapping("/patients")
    @Operation(summary = "Register a new patient")
    public ResponseEntity<ApiResponse<PatientDto>> registerPatient(@Valid @RequestBody PatientDto request) {
        PatientDto patient = hospitalService.registerPatient(request);
        return new ResponseEntity<>(ApiResponse.success(patient, "Patient registered successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/patients/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse<PatientDto>> getPatientById(@PathVariable UUID id) {
        PatientDto patient = hospitalService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.success(patient));
    }

    @GetMapping("/patients")
    @Operation(summary = "List tenant patients with search & blood group filter")
    public ResponseEntity<ApiResponse<PageResponse<PatientDto>>> getPatients(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String bloodGroup,
            Pageable pageable) {
        PageResponse<PatientDto> patients = hospitalService.getTenantPatients(search, bloodGroup, pageable);
        return ResponseEntity.ok(ApiResponse.success(patients));
    }

    // DOCTORS
    @PostMapping("/doctors")
    @Operation(summary = "Register a new doctor")
    public ResponseEntity<ApiResponse<DoctorDto>> addDoctor(@Valid @RequestBody DoctorDto request) {
        DoctorDto doctor = hospitalService.addDoctor(request);
        return new ResponseEntity<>(ApiResponse.success(doctor, "Doctor registered successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/doctors/{id}")
    @Operation(summary = "Update doctor details")
    public ResponseEntity<ApiResponse<DoctorDto>> updateDoctor(@PathVariable UUID id, @Valid @RequestBody DoctorDto request) {
        DoctorDto updated = hospitalService.updateDoctor(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Doctor updated successfully"));
    }

    @DeleteMapping("/doctors/{id}")
    @Operation(summary = "Delete doctor")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable UUID id) {
        hospitalService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Doctor deleted successfully"));
    }

    @GetMapping("/doctors")
    @Operation(summary = "List tenant doctors")
    public ResponseEntity<ApiResponse<PageResponse<DoctorDto>>> getDoctors(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        PageResponse<DoctorDto> doctors = hospitalService.getTenantDoctors(search, pageable);
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }

    // APPOINTMENTS
    @PostMapping("/appointments")
    @Operation(summary = "Schedule a doctor appointment")
    public ResponseEntity<ApiResponse<AppointmentDto>> scheduleAppointment(@Valid @RequestBody AppointmentDto request) {
        AppointmentDto appointment = hospitalService.scheduleAppointment(request);
        return new ResponseEntity<>(ApiResponse.success(appointment, "Appointment scheduled"), HttpStatus.CREATED);
    }

    @GetMapping("/appointments")
    @Operation(summary = "List tenant appointments")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentDto>>> getAppointments(Pageable pageable) {
        PageResponse<AppointmentDto> appointments = hospitalService.getTenantAppointments(pageable);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    // EHR MEDICAL RECORDS
    @PostMapping("/ehr")
    @Operation(summary = "Create an EHR medical record for a patient")
    public ResponseEntity<ApiResponse<MedicalRecordDto>> createMedicalRecord(@Valid @RequestBody MedicalRecordDto request) {
        MedicalRecordDto record = hospitalService.createMedicalRecord(request);
        return new ResponseEntity<>(ApiResponse.success(record, "Medical record created"), HttpStatus.CREATED);
    }

    @GetMapping("/ehr/patient/{patientId}")
    @Operation(summary = "Get medical records history for a patient")
    public ResponseEntity<ApiResponse<PageResponse<MedicalRecordDto>>> getPatientMedicalRecords(
            @PathVariable UUID patientId,
            Pageable pageable) {
        PageResponse<MedicalRecordDto> records = hospitalService.getPatientMedicalRecords(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    // PHARMACY MEDICINES
    @PostMapping("/medicines")
    @Operation(summary = "Add new medicine to inventory")
    public ResponseEntity<ApiResponse<MedicineDto>> addMedicine(@Valid @RequestBody MedicineDto request) {
        MedicineDto medicine = hospitalService.addMedicine(request);
        return new ResponseEntity<>(ApiResponse.success(medicine, "Medicine added to inventory"), HttpStatus.CREATED);
    }

    @GetMapping("/medicines")
    @Operation(summary = "List pharmacy medicine inventory")
    public ResponseEntity<ApiResponse<PageResponse<MedicineDto>>> getMedicines(Pageable pageable) {
        PageResponse<MedicineDto> medicines = hospitalService.getMedicines(pageable);
        return ResponseEntity.ok(ApiResponse.success(medicines));
    }
}
