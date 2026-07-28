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
@Tag(name = "Hospital Management", description = "Multi-Tenant Hospital Management APIs (Patients, Doctors, Appointments, Scheduling, Billing, Payments, Insurance Claims, EHR, Pharmacy)")
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

    @PostMapping("/doctors/availability")
    @Operation(summary = "Configure doctor weekly availability slot")
    public ResponseEntity<ApiResponse<DoctorAvailabilityDto>> setDoctorAvailability(@Valid @RequestBody DoctorAvailabilityDto request) {
        DoctorAvailabilityDto availability = hospitalService.setDoctorAvailability(request);
        return new ResponseEntity<>(ApiResponse.success(availability, "Doctor availability set"), HttpStatus.CREATED);
    }

    @GetMapping("/doctors/{doctorId}/availability")
    @Operation(summary = "Get doctor weekly availability schedule")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityDto>>> getDoctorAvailabilities(@PathVariable UUID doctorId) {
        List<DoctorAvailabilityDto> list = hospitalService.getDoctorAvailabilities(doctorId);
        return ResponseEntity.ok(ApiResponse.success(list));
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

    // APPOINTMENTS & SCHEDULING
    @PostMapping("/appointments")
    @Operation(summary = "Schedule a doctor appointment")
    public ResponseEntity<ApiResponse<AppointmentDto>> scheduleAppointment(@Valid @RequestBody AppointmentDto request) {
        AppointmentDto appointment = hospitalService.scheduleAppointment(request);
        return new ResponseEntity<>(ApiResponse.success(appointment, "Appointment scheduled"), HttpStatus.CREATED);
    }

    @PutMapping("/appointments/{id}/status")
    @Operation(summary = "Update appointment status (SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW)")
    public ResponseEntity<ApiResponse<AppointmentDto>> updateAppointmentStatus(@PathVariable UUID id, @RequestParam String status) {
        AppointmentDto updated = hospitalService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Appointment status updated"));
    }

    @PostMapping("/appointments/reschedule/{id}")
    @Operation(summary = "Reschedule an existing appointment")
    public ResponseEntity<ApiResponse<AppointmentDto>> rescheduleAppointment(@PathVariable UUID id, @Valid @RequestBody AppointmentDto request) {
        AppointmentDto rescheduled = hospitalService.rescheduleAppointment(id, request);
        return ResponseEntity.ok(ApiResponse.success(rescheduled, "Appointment rescheduled successfully"));
    }

    @GetMapping("/appointments")
    @Operation(summary = "List tenant appointments with status & calendar dates")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentDto>>> getAppointments(Pageable pageable) {
        PageResponse<AppointmentDto> appointments = hospitalService.getTenantAppointments(pageable);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    // WAITING LIST
    @PostMapping("/appointments/waiting-list")
    @Operation(summary = "Add patient to appointment waiting list")
    public ResponseEntity<ApiResponse<AppointmentWaitingListDto>> addToWaitingList(@Valid @RequestBody AppointmentWaitingListDto request) {
        AppointmentWaitingListDto waiting = hospitalService.addToWaitingList(request);
        return new ResponseEntity<>(ApiResponse.success(waiting, "Patient added to waiting list"), HttpStatus.CREATED);
    }

    @GetMapping("/appointments/waiting-list")
    @Operation(summary = "List appointment waiting queue")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentWaitingListDto>>> getWaitingList(Pageable pageable) {
        PageResponse<AppointmentWaitingListDto> waitingList = hospitalService.getWaitingList(pageable);
        return ResponseEntity.ok(ApiResponse.success(waitingList));
    }

    // REMINDERS
    @PostMapping("/appointments/{id}/remind")
    @Operation(summary = "Send Email or SMS appointment reminder")
    public ResponseEntity<ApiResponse<ReminderLogDto>> sendAppointmentReminder(@PathVariable UUID id, @RequestParam(defaultValue = "EMAIL") String channel) {
        ReminderLogDto reminder = hospitalService.sendAppointmentReminder(id, channel);
        return ResponseEntity.ok(ApiResponse.success(reminder, "Reminder sent successfully via " + channel));
    }

    @GetMapping("/appointments/reminders")
    @Operation(summary = "List appointment reminder audit logs")
    public ResponseEntity<ApiResponse<PageResponse<ReminderLogDto>>> getReminderLogs(Pageable pageable) {
        PageResponse<ReminderLogDto> logs = hospitalService.getReminderLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }

    // BILLING & INVOICING
    @PostMapping("/invoices")
    @Operation(summary = "Create patient billing invoice with itemized breakdown")
    public ResponseEntity<ApiResponse<InvoiceDto>> createInvoice(@Valid @RequestBody InvoiceDto request) {
        InvoiceDto invoice = hospitalService.createInvoice(request);
        return new ResponseEntity<>(ApiResponse.success(invoice, "Invoice generated successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/invoices")
    @Operation(summary = "List tenant billing invoices")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceDto>>> getInvoices(Pageable pageable) {
        PageResponse<InvoiceDto> invoices = hospitalService.getInvoices(pageable);
        return ResponseEntity.ok(ApiResponse.success(invoices));
    }

    // PAYMENTS & STRIPE ABSTRACTION
    @PostMapping("/payments")
    @Operation(summary = "Process patient invoice payment (Cash, Credit Card, Stripe abstraction, Insurance)")
    public ResponseEntity<ApiResponse<PaymentDto>> processPayment(@Valid @RequestBody PaymentDto request) {
        PaymentDto payment = hospitalService.processPayment(request);
        return new ResponseEntity<>(ApiResponse.success(payment, "Payment processed successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/payments")
    @Operation(summary = "List tenant payment transaction records")
    public ResponseEntity<ApiResponse<PageResponse<PaymentDto>>> getPayments(Pageable pageable) {
        PageResponse<PaymentDto> payments = hospitalService.getPayments(pageable);
        return ResponseEntity.ok(ApiResponse.success(payments));
    }

    // INSURANCE PROVIDERS & CLAIMS
    @PostMapping("/insurance/providers")
    @Operation(summary = "Register an insurance provider")
    public ResponseEntity<ApiResponse<InsuranceProviderDto>> addInsuranceProvider(@Valid @RequestBody InsuranceProviderDto request) {
        InsuranceProviderDto provider = hospitalService.addInsuranceProvider(request);
        return new ResponseEntity<>(ApiResponse.success(provider, "Insurance provider registered"), HttpStatus.CREATED);
    }

    @GetMapping("/insurance/providers")
    @Operation(summary = "List insurance providers")
    public ResponseEntity<ApiResponse<List<InsuranceProviderDto>>> getInsuranceProviders() {
        List<InsuranceProviderDto> providers = hospitalService.getInsuranceProviders();
        return ResponseEntity.ok(ApiResponse.success(providers));
    }

    @PostMapping("/insurance/claims")
    @Operation(summary = "Submit insurance claim for patient invoice")
    public ResponseEntity<ApiResponse<InsuranceClaimDto>> submitInsuranceClaim(@Valid @RequestBody InsuranceClaimDto request) {
        InsuranceClaimDto claim = hospitalService.submitInsuranceClaim(request);
        return new ResponseEntity<>(ApiResponse.success(claim, "Insurance claim submitted"), HttpStatus.CREATED);
    }

    @GetMapping("/insurance/claims")
    @Operation(summary = "List patient insurance claims")
    public ResponseEntity<ApiResponse<PageResponse<InsuranceClaimDto>>> getInsuranceClaims(Pageable pageable) {
        PageResponse<InsuranceClaimDto> claims = hospitalService.getInsuranceClaims(pageable);
        return ResponseEntity.ok(ApiResponse.success(claims));
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
