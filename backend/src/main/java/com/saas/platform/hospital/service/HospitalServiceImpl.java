package com.saas.platform.hospital.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.common.exception.ResourceNotFoundException;
import com.saas.platform.hospital.dto.AppointmentDto;
import com.saas.platform.hospital.dto.MedicineDto;
import com.saas.platform.hospital.dto.PatientDto;
import com.saas.platform.hospital.entity.Appointment;
import com.saas.platform.hospital.entity.AppointmentStatus;
import com.saas.platform.hospital.entity.Medicine;
import com.saas.platform.hospital.entity.Patient;
import com.saas.platform.hospital.repository.AppointmentRepository;
import com.saas.platform.hospital.repository.MedicineRepository;
import com.saas.platform.hospital.repository.PatientRepository;
import com.saas.platform.tenant.context.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public PatientDto registerPatient(PatientDto request) {
        UUID tenantId = TenantContext.getTenantId();
        String patientCode = "PAT-" + System.currentTimeMillis() % 100000;

        Patient patient = Patient.builder()
                .tenantId(tenantId)
                .patientCode(patientCode)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .address(request.getAddress())
                .emergencyContact(request.getEmergencyContact())
                .insuranceProvider(request.getInsuranceProvider())
                .insurancePolicyNumber(request.getInsurancePolicyNumber())
                .build();

        Patient saved = patientRepository.save(patient);
        log.info("Registered patient: id={}, code={}", saved.getId(), saved.getPatientCode());
        return mapPatientToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        return mapPatientToDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PatientDto> getTenantPatients(Pageable pageable) {
        UUID tenantId = TenantContext.getTenantId();
        Page<PatientDto> page = patientRepository.findByTenantId(tenantId, pageable).map(this::mapPatientToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public AppointmentDto scheduleAppointment(AppointmentDto request) {
        UUID tenantId = TenantContext.getTenantId();

        Appointment appointment = Appointment.builder()
                .tenantId(tenantId)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .departmentId(request.getDepartmentId())
                .appointmentDate(request.getAppointmentDate())
                .timeSlot(request.getTimeSlot())
                .status(AppointmentStatus.SCHEDULED)
                .type(request.getType() != null ? request.getType() : "IN_PERSON")
                .reason(request.getReason())
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment scheduled: id={}", saved.getId());
        return mapAppointmentToDto(saved);
    }

    @Override
    @Transactional
    public AppointmentDto updateAppointmentStatus(UUID appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        Appointment updated = appointmentRepository.save(appointment);
        return mapAppointmentToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AppointmentDto> getTenantAppointments(Pageable pageable) {
        UUID tenantId = TenantContext.getTenantId();
        Page<AppointmentDto> page = appointmentRepository.findByTenantId(tenantId, pageable).map(this::mapAppointmentToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public MedicineDto addMedicine(MedicineDto request) {
        UUID tenantId = TenantContext.getTenantId();
        Medicine medicine = Medicine.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .genericName(request.getGenericName())
                .category(request.getCategory())
                .batchNumber(request.getBatchNumber())
                .stockQuantity(request.getStockQuantity())
                .unitPrice(request.getUnitPrice())
                .expiryDate(request.getExpiryDate())
                .manufacturer(request.getManufacturer())
                .build();

        Medicine saved = medicineRepository.save(medicine);
        return mapMedicineToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MedicineDto> getMedicines(Pageable pageable) {
        UUID tenantId = TenantContext.getTenantId();
        Page<MedicineDto> page = medicineRepository.findByTenantId(tenantId, pageable).map(this::mapMedicineToDto);
        return PageResponse.from(page);
    }

    private PatientDto mapPatientToDto(Patient p) {
        return PatientDto.builder()
                .id(p.getId())
                .patientCode(p.getPatientCode())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .dateOfBirth(p.getDateOfBirth())
                .gender(p.getGender())
                .bloodGroup(p.getBloodGroup())
                .address(p.getAddress())
                .emergencyContact(p.getEmergencyContact())
                .insuranceProvider(p.getInsuranceProvider())
                .insurancePolicyNumber(p.getInsurancePolicyNumber())
                .build();
    }

    private AppointmentDto mapAppointmentToDto(Appointment a) {
        return AppointmentDto.builder()
                .id(a.getId())
                .patientId(a.getPatientId())
                .doctorId(a.getDoctorId())
                .departmentId(a.getDepartmentId())
                .appointmentDate(a.getAppointmentDate())
                .timeSlot(a.getTimeSlot())
                .status(a.getStatus())
                .type(a.getType())
                .reason(a.getReason())
                .build();
    }

    private MedicineDto mapMedicineToDto(Medicine m) {
        return MedicineDto.builder()
                .id(m.getId())
                .name(m.getName())
                .genericName(m.getGenericName())
                .category(m.getCategory())
                .batchNumber(m.getBatchNumber())
                .stockQuantity(m.getStockQuantity())
                .unitPrice(m.getUnitPrice())
                .expiryDate(m.getExpiryDate())
                .manufacturer(m.getManufacturer())
                .build();
    }
}
