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

    DoctorAvailabilityDto setDoctorAvailability(DoctorAvailabilityDto request);
    List<DoctorAvailabilityDto> getDoctorAvailabilities(UUID doctorId);

    AppointmentDto scheduleAppointment(AppointmentDto request);
    AppointmentDto updateAppointmentStatus(UUID appointmentId, String status);
    AppointmentDto rescheduleAppointment(UUID appointmentId, AppointmentDto request);
    PageResponse<AppointmentDto> getTenantAppointments(Pageable pageable);

    AppointmentWaitingListDto addToWaitingList(AppointmentWaitingListDto request);
    PageResponse<AppointmentWaitingListDto> getWaitingList(Pageable pageable);

    ReminderLogDto sendAppointmentReminder(UUID appointmentId, String channel);
    PageResponse<ReminderLogDto> getReminderLogs(Pageable pageable);

    InvoiceDto createInvoice(InvoiceDto request);
    PageResponse<InvoiceDto> getInvoices(Pageable pageable);

    PaymentDto processPayment(PaymentDto request);
    PageResponse<PaymentDto> getPayments(Pageable pageable);

    InsuranceProviderDto addInsuranceProvider(InsuranceProviderDto request);
    List<InsuranceProviderDto> getInsuranceProviders();

    InsuranceClaimDto submitInsuranceClaim(InsuranceClaimDto request);
    PageResponse<InsuranceClaimDto> getInsuranceClaims(Pageable pageable);

    EhrRecordDto saveEhrRecord(EhrRecordDto request);
    PageResponse<EhrRecordDto> getPatientEhrRecords(UUID patientId, Pageable pageable);

    MedicineDto addMedicine(MedicineDto request);
    PageResponse<MedicineDto> getMedicines(Pageable pageable);
}
