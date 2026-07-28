package com.saas.platform.hospital.service;

import com.saas.platform.common.dto.PageResponse;
import com.saas.platform.common.exception.ResourceNotFoundException;
import com.saas.platform.hospital.dto.*;
import com.saas.platform.hospital.entity.*;
import com.saas.platform.hospital.repository.*;
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

    private static final UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicineRepository medicineRepository;

    private UUID resolveTenantId() {
        UUID tenantId = TenantContext.getTenantId();
        return tenantId != null ? tenantId : DEFAULT_TENANT_ID;
    }

    @Override
    @Transactional
    public PatientDto registerPatient(PatientDto request) {
        UUID tenantId = resolveTenantId();
        String patientCode = request.getPatientCode() != null && !request.getPatientCode().isBlank() 
                ? request.getPatientCode() 
                : "PAT-" + (System.currentTimeMillis() % 100000);

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
    public PageResponse<PatientDto> getTenantPatients(String search, String bloodGroup, Pageable pageable) {
        UUID tenantId = resolveTenantId();
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        String bgParam = (bloodGroup != null && !bloodGroup.isBlank()) ? bloodGroup.trim() : null;

        Page<PatientDto> page = patientRepository.findByTenantIdWithFilters(tenantId, searchParam, bgParam, pageable)
                .map(this::mapPatientToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public DoctorDto addDoctor(DoctorDto request) {
        UUID tenantId = resolveTenantId();
        Doctor doctor = Doctor.builder()
                .tenantId(tenantId)
                .userId(request.getUserId() != null ? request.getUserId() : UUID.randomUUID())
                .departmentId(request.getDepartmentId())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .consultationFee(request.getConsultationFee())
                .licenseNumber(request.getLicenseNumber())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .build();

        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor registered: id={}, license={}", saved.getId(), saved.getLicenseNumber());
        return mapDoctorToDto(saved);
    }

    @Override
    @Transactional
    public DoctorDto updateDoctor(UUID id, DoctorDto request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setLicenseNumber(request.getLicenseNumber());
        if (request.getIsAvailable() != null) {
            doctor.setIsAvailable(request.getIsAvailable());
        }

        Doctor updated = doctorRepository.save(doctor);
        return mapDoctorToDto(updated);
    }

    @Override
    @Transactional
    public void deleteDoctor(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        doctorRepository.delete(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DoctorDto> getTenantDoctors(String search, Pageable pageable) {
        UUID tenantId = resolveTenantId();
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;

        Page<DoctorDto> page = doctorRepository.findByTenantIdWithFilters(tenantId, searchParam, pageable)
                .map(this::mapDoctorToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public AppointmentDto scheduleAppointment(AppointmentDto request) {
        UUID tenantId = resolveTenantId();

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
        UUID tenantId = resolveTenantId();
        Page<AppointmentDto> page = appointmentRepository.findByTenantId(tenantId, pageable).map(this::mapAppointmentToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public MedicalRecordDto createMedicalRecord(MedicalRecordDto request) {
        UUID tenantId = resolveTenantId();

        MedicalRecord record = MedicalRecord.builder()
                .tenantId(tenantId)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentId(request.getAppointmentId())
                .symptoms(request.getSymptoms())
                .diagnosis(request.getDiagnosis())
                .vitalBp(request.getVitalBp())
                .vitalHeartRate(request.getVitalHeartRate())
                .vitalTemp(request.getVitalTemp())
                .vitalWeight(request.getVitalWeight())
                .doctorNotes(request.getDoctorNotes())
                .build();

        MedicalRecord saved = medicalRecordRepository.save(record);
        log.info("Medical record created: id={}", saved.getId());
        return mapMedicalRecordToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MedicalRecordDto> getPatientMedicalRecords(UUID patientId, Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<MedicalRecordDto> page = medicalRecordRepository.findByTenantIdAndPatientId(tenantId, patientId, pageable)
                .map(this::mapMedicalRecordToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public MedicineDto addMedicine(MedicineDto request) {
        UUID tenantId = resolveTenantId();
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
        UUID tenantId = resolveTenantId();
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

    private DoctorDto mapDoctorToDto(Doctor d) {
        return DoctorDto.builder()
                .id(d.getId())
                .userId(d.getUserId())
                .departmentId(d.getDepartmentId())
                .specialization(d.getSpecialization())
                .qualification(d.getQualification())
                .consultationFee(d.getConsultationFee())
                .licenseNumber(d.getLicenseNumber())
                .isAvailable(d.getIsAvailable())
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

    private MedicalRecordDto mapMedicalRecordToDto(MedicalRecord m) {
        return MedicalRecordDto.builder()
                .id(m.getId())
                .patientId(m.getPatientId())
                .doctorId(m.getDoctorId())
                .appointmentId(m.getAppointmentId())
                .symptoms(m.getSymptoms())
                .diagnosis(m.getDiagnosis())
                .vitalBp(m.getVitalBp())
                .vitalHeartRate(m.getVitalHeartRate())
                .vitalTemp(m.getVitalTemp())
                .vitalWeight(m.getVitalWeight())
                .doctorNotes(m.getDoctorNotes())
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
