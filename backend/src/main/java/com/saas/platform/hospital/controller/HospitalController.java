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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
@Tag(name = "Hospital Management", description = "Multi-Tenant Hospital Management APIs (Patients, Doctors, Specializations, Shifts, EHR, Prescriptions)")
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

    // SPECIALIZATIONS
    @PostMapping("/specializations")
    @Operation(summary = "Create medical specialization")
    public ResponseEntity<ApiResponse<SpecializationDto>> createSpecialization(@Valid @RequestBody SpecializationDto request) {
        SpecializationDto spec = hospitalService.createSpecialization(request);
        return new ResponseEntity<>(ApiResponse.success(spec, "Specialization created"), HttpStatus.CREATED);
    }

    @GetMapping("/specializations")
    @Operation(summary = "List all medical specializations")
    public ResponseEntity<ApiResponse<List<SpecializationDto>>> getSpecializations() {
        List<SpecializationDto> list = hospitalService.getSpecializations();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    // SHIFTS
    @PostMapping("/shifts")
    @Operation(summary = "Create work shift")
    public ResponseEntity<ApiResponse<ShiftDto>> createShift(@Valid @RequestBody ShiftDto request) {
        ShiftDto shift = hospitalService.createShift(request);
        return new ResponseEntity<>(ApiResponse.success(shift, "Shift created"), HttpStatus.CREATED);
    }

    @GetMapping("/shifts")
    @Operation(summary = "List hospital shifts")
    public ResponseEntity<ApiResponse<List<ShiftDto>>> getShifts() {
        List<ShiftDto> list = hospitalService.getShifts();
        return ResponseEntity.ok(ApiResponse.success(list));
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

    // EHR RECORDS
    @PostMapping("/ehr/record")
    @Operation(summary = "Create an advanced EHR record")
    public ResponseEntity<ApiResponse<EhrRecordDto>> saveEhrRecord(@Valid @RequestBody EhrRecordDto request) {
        EhrRecordDto record = hospitalService.saveEhrRecord(request);
        return new ResponseEntity<>(ApiResponse.success(record, "EHR record saved"), HttpStatus.CREATED);
    }

    @GetMapping("/ehr/patient/{patientId}/records")
    @Operation(summary = "Get patient EHR records timeline")
    public ResponseEntity<ApiResponse<PageResponse<EhrRecordDto>>> getPatientEhrRecords(
            @PathVariable UUID patientId,
            Pageable pageable) {
        PageResponse<EhrRecordDto> records = hospitalService.getPatientEhrRecords(patientId, pageable);
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
