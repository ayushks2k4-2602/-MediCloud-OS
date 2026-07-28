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

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private static final UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final ShiftRepository shiftRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentWaitingListRepository appointmentWaitingListRepository;
    private final ReminderLogRepository reminderLogRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final InsuranceProviderRepository insuranceProviderRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;
    private final LabTestCatalogRepository labTestCatalogRepository;
    private final LabOrderRepository labOrderRepository;
    private final LabSampleRepository labSampleRepository;
    private final LabTestResultRepository labTestResultRepository;
    private final PharmacySupplierRepository pharmacySupplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PrescriptionFulfillmentRepository prescriptionFulfillmentRepository;
    private final StockMovementRepository stockMovementRepository;
    private final RadiologyRequestRepository radiologyRequestRepository;
    private final WardRepository wardRepository;
    private final BedRepository bedRepository;
    private final HospitalAuditLogRepository hospitalAuditLogRepository;
    private final AiClinicalCopilotRepository aiClinicalCopilotRepository;
    private final EhrRecordRepository ehrRecordRepository;
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
                .specializationId(request.getSpecializationId())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 5)
                .contactNumber(request.getContactNumber())
                .profilePhotoUrl(request.getProfilePhotoUrl())
                .employmentStatus(request.getEmploymentStatus() != null ? request.getEmploymentStatus() : "FULL_TIME")
                .consultationFee(request.getConsultationFee())
                .licenseNumber(request.getLicenseNumber())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .build();

        Doctor saved = doctorRepository.save(doctor);
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
        if (request.getExperienceYears() != null) doctor.setExperienceYears(request.getExperienceYears());
        if (request.getContactNumber() != null) doctor.setContactNumber(request.getContactNumber());
        if (request.getEmploymentStatus() != null) doctor.setEmploymentStatus(request.getEmploymentStatus());
        if (request.getIsAvailable() != null) doctor.setIsAvailable(request.getIsAvailable());

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
    public SpecializationDto createSpecialization(SpecializationDto request) {
        UUID tenantId = resolveTenantId();
        Specialization spec = Specialization.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .build();

        Specialization saved = specializationRepository.save(spec);
        return mapSpecializationToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecializationDto> getSpecializations() {
        UUID tenantId = resolveTenantId();
        return specializationRepository.findByTenantId(tenantId).stream()
                .map(this::mapSpecializationToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShiftDto createShift(ShiftDto request) {
        UUID tenantId = resolveTenantId();
        Shift shift = Shift.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .workingDays(request.getWorkingDays() != null ? request.getWorkingDays() : "Mon,Tue,Wed,Thu,Fri")
                .departmentId(request.getDepartmentId())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .build();

        Shift saved = shiftRepository.save(shift);
        return mapShiftToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDto> getShifts() {
        UUID tenantId = resolveTenantId();
        return shiftRepository.findByTenantId(tenantId).stream()
                .map(this::mapShiftToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorAvailabilityDto setDoctorAvailability(DoctorAvailabilityDto request) {
        UUID tenantId = resolveTenantId();
        DoctorAvailability availability = DoctorAvailability.builder()
                .tenantId(tenantId)
                .doctorId(request.getDoctorId())
                .dayOfWeek(request.getDayOfWeek().toUpperCase())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotDurationMinutes(request.getSlotDurationMinutes() != null ? request.getSlotDurationMinutes() : 30)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        DoctorAvailability saved = doctorAvailabilityRepository.save(availability);
        return mapDoctorAvailabilityToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailabilityDto> getDoctorAvailabilities(UUID doctorId) {
        UUID tenantId = resolveTenantId();
        return doctorAvailabilityRepository.findByTenantIdAndDoctorId(tenantId, doctorId).stream()
                .map(this::mapDoctorAvailabilityToDto)
                .collect(Collectors.toList());
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
                .status(request.getStatus() != null ? request.getStatus() : AppointmentStatus.SCHEDULED)
                .type(request.getType() != null ? request.getType() : "IN_PERSON")
                .reason(request.getReason())
                .cancellationReason(request.getCancellationReason())
                .rescheduledFromId(request.getRescheduledFromId())
                .reminderSentEmail(false)
                .reminderSentSms(false)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
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
    @Transactional
    public AppointmentDto rescheduleAppointment(UUID appointmentId, AppointmentDto request) {
        Appointment oldAppt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        oldAppt.setStatus(AppointmentStatus.CANCELLED);
        oldAppt.setCancellationReason("Rescheduled to new date: " + request.getAppointmentDate() + " " + request.getTimeSlot());
        appointmentRepository.save(oldAppt);

        request.setRescheduledFromId(oldAppt.getId());
        return scheduleAppointment(request);
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
    public AppointmentWaitingListDto addToWaitingList(AppointmentWaitingListDto request) {
        UUID tenantId = resolveTenantId();
        AppointmentWaitingList waiting = AppointmentWaitingList.builder()
                .tenantId(tenantId)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .requestedDate(request.getRequestedDate())
                .preferredTimeSlot(request.getPreferredTimeSlot())
                .priorityNotes(request.getPriorityNotes())
                .status("WAITING")
                .build();

        AppointmentWaitingList saved = appointmentWaitingListRepository.save(waiting);
        return mapWaitingToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AppointmentWaitingListDto> getWaitingList(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<AppointmentWaitingListDto> page = appointmentWaitingListRepository.findByTenantId(tenantId, pageable).map(this::mapWaitingToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public ReminderLogDto sendAppointmentReminder(UUID appointmentId, String channel) {
        UUID tenantId = resolveTenantId();
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        Patient patient = patientRepository.findById(appt.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", appt.getPatientId()));

        String recipient = "SMS".equalsIgnoreCase(channel) ? patient.getPhone() : patient.getEmail();
        String message = "Reminder: Your appointment is scheduled on " + appt.getAppointmentDate() + " at " + appt.getTimeSlot() + " with Dr. Vishnu Tiwari Health Network.";

        if ("SMS".equalsIgnoreCase(channel)) {
            appt.setReminderSentSms(true);
        } else {
            appt.setReminderSentEmail(true);
        }
        appointmentRepository.save(appt);

        ReminderLog log = ReminderLog.builder()
                .tenantId(tenantId)
                .appointmentId(appointmentId)
                .channel(channel.toUpperCase())
                .recipient(recipient != null ? recipient : "unknown@ayushhealth.com")
                .message(message)
                .status("SENT")
                .build();

        ReminderLog saved = reminderLogRepository.save(log);
        return mapReminderToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReminderLogDto> getReminderLogs(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<ReminderLogDto> page = reminderLogRepository.findByTenantId(tenantId, pageable).map(this::mapReminderToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public InvoiceDto createInvoice(InvoiceDto request) {
        UUID tenantId = resolveTenantId();
        String invoiceNum = "INV-" + (System.currentTimeMillis() % 1000000);

        BigDecimal subtotal = BigDecimal.ZERO;
        List<InvoiceItem> items = new ArrayList<>();

        if (request.getItems() != null) {
            for (InvoiceItemDto itemDto : request.getItems()) {
                BigDecimal total = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
                subtotal = subtotal.add(total);
            }
        }

        BigDecimal tax = request.getTaxAmount() != null ? request.getTaxAmount() : subtotal.multiply(new BigDecimal("0.10"));
        BigDecimal discount = request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal totalAmount = subtotal.add(tax).subtract(discount);

        Invoice invoice = Invoice.builder()
                .tenantId(tenantId)
                .invoiceNumber(invoiceNum)
                .patientId(request.getPatientId())
                .appointmentId(request.getAppointmentId())
                .subtotal(subtotal)
                .taxAmount(tax)
                .discountAmount(discount)
                .totalAmount(totalAmount)
                .status("UNPAID")
                .dueDate(request.getDueDate())
                .build();

        if (request.getItems() != null) {
            for (InvoiceItemDto itemDto : request.getItems()) {
                BigDecimal total = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
                InvoiceItem item = InvoiceItem.builder()
                        .invoice(invoice)
                        .description(itemDto.getDescription())
                        .quantity(itemDto.getQuantity())
                        .unitPrice(itemDto.getUnitPrice())
                        .totalPrice(total)
                        .build();
                items.add(item);
            }
        }
        invoice.setItems(items);

        Invoice saved = invoiceRepository.save(invoice);
        return mapInvoiceToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InvoiceDto> getInvoices(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<InvoiceDto> page = invoiceRepository.findByTenantId(tenantId, pageable).map(this::mapInvoiceToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public PaymentDto processPayment(PaymentDto request) {
        UUID tenantId = resolveTenantId();
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", request.getInvoiceId()));

        String payNum = "PAY-" + (System.currentTimeMillis() % 1000000);
        Payment payment = Payment.builder()
                .tenantId(tenantId)
                .invoiceId(invoice.getId())
                .paymentNumber(payNum)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionReference(request.getTransactionReference() != null ? request.getTransactionReference() : "TXN-" + System.currentTimeMillis())
                .status("COMPLETED")
                .build();

        Payment saved = paymentRepository.save(payment);

        invoice.setStatus("PAID");
        invoiceRepository.save(invoice);

        return mapPaymentToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PaymentDto> getPayments(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<PaymentDto> page = paymentRepository.findByTenantId(tenantId, pageable).map(this::mapPaymentToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public InsuranceProviderDto addInsuranceProvider(InsuranceProviderDto request) {
        UUID tenantId = resolveTenantId();
        InsuranceProvider provider = InsuranceProvider.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .code(request.getCode())
                .contactPhone(request.getContactPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();

        InsuranceProvider saved = insuranceProviderRepository.save(provider);
        return mapProviderToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceProviderDto> getInsuranceProviders() {
        UUID tenantId = resolveTenantId();
        return insuranceProviderRepository.findByTenantId(tenantId).stream()
                .map(this::mapProviderToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InsuranceClaimDto submitInsuranceClaim(InsuranceClaimDto request) {
        UUID tenantId = resolveTenantId();
        String claimNum = "CLM-" + (System.currentTimeMillis() % 1000000);

        InsuranceClaim claim = InsuranceClaim.builder()
                .tenantId(tenantId)
                .claimNumber(claimNum)
                .patientId(request.getPatientId())
                .insuranceProviderId(request.getInsuranceProviderId())
                .invoiceId(request.getInvoiceId())
                .claimAmount(request.getClaimAmount())
                .approvedAmount(BigDecimal.ZERO)
                .status("SUBMITTED")
                .notes(request.getNotes())
                .build();

        InsuranceClaim saved = insuranceClaimRepository.save(claim);
        return mapClaimToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InsuranceClaimDto> getInsuranceClaims(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<InsuranceClaimDto> page = insuranceClaimRepository.findByTenantId(tenantId, pageable).map(this::mapClaimToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public LabTestCatalogDto addLabTest(LabTestCatalogDto request) {
        UUID tenantId = resolveTenantId();
        LabTestCatalog test = LabTestCatalog.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .code(request.getCode())
                .category(request.getCategory())
                .price(request.getPrice())
                .sampleType(request.getSampleType())
                .normalRange(request.getNormalRange())
                .unit(request.getUnit())
                .build();

        LabTestCatalog saved = labTestCatalogRepository.save(test);
        return mapLabTestToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestCatalogDto> getLabTestCatalog() {
        UUID tenantId = resolveTenantId();
        return labTestCatalogRepository.findByTenantId(tenantId).stream()
                .map(this::mapLabTestToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LabOrderDto createLabOrder(LabOrderDto request) {
        UUID tenantId = resolveTenantId();
        String orderNum = "LAB-" + (System.currentTimeMillis() % 1000000);

        LabOrder order = LabOrder.builder()
                .tenantId(tenantId)
                .orderNumber(orderNum)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentId(request.getAppointmentId())
                .status("ORDERED")
                .totalAmount(request.getTotalAmount() != null ? request.getTotalAmount() : BigDecimal.ZERO)
                .build();

        LabOrder saved = labOrderRepository.save(order);
        return mapLabOrderToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<LabOrderDto> getLabOrders(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<LabOrderDto> page = labOrderRepository.findByTenantId(tenantId, pageable).map(this::mapLabOrderToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public LabSampleDto collectLabSample(LabSampleDto request) {
        UUID tenantId = resolveTenantId();
        String sampleCode = "SMP-" + (System.currentTimeMillis() % 1000000);

        LabSample sample = LabSample.builder()
                .tenantId(tenantId)
                .sampleCode(sampleCode)
                .labOrderId(request.getLabOrderId())
                .specimenType(request.getSpecimenType())
                .status("COLLECTED")
                .collectedAt(ZonedDateTime.now())
                .build();

        LabSample saved = labSampleRepository.save(sample);

        LabOrder order = labOrderRepository.findById(request.getLabOrderId()).orElse(null);
        if (order != null) {
            order.setStatus("SAMPLE_COLLECTED");
            labOrderRepository.save(order);
        }

        return mapLabSampleToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<LabSampleDto> getLabSamples(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<LabSampleDto> page = labSampleRepository.findByTenantId(tenantId, pageable).map(this::mapLabSampleToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public LabTestResultDto enterLabResult(LabTestResultDto request) {
        UUID tenantId = resolveTenantId();

        LabTestResult result = LabTestResult.builder()
                .tenantId(tenantId)
                .labOrderId(request.getLabOrderId())
                .testCatalogId(request.getTestCatalogId())
                .resultValue(request.getResultValue())
                .normalRange(request.getNormalRange())
                .unit(request.getUnit())
                .isCritical(request.getIsCritical() != null ? request.getIsCritical() : false)
                .status("APPROVED")
                .pathologistNotes(request.getPathologistNotes())
                .approvedAt(ZonedDateTime.now())
                .build();

        LabTestResult saved = labTestResultRepository.save(result);

        LabOrder order = labOrderRepository.findById(request.getLabOrderId()).orElse(null);
        if (order != null) {
            order.setStatus("COMPLETED");
            labOrderRepository.save(order);
        }

        return mapLabResultToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<LabTestResultDto> getLabResults(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<LabTestResultDto> page = labTestResultRepository.findByTenantId(tenantId, pageable).map(this::mapLabResultToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public PharmacySupplierDto addPharmacySupplier(PharmacySupplierDto request) {
        UUID tenantId = resolveTenantId();
        PharmacySupplier supplier = PharmacySupplier.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .code(request.getCode())
                .contactPhone(request.getContactPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();

        PharmacySupplier saved = pharmacySupplierRepository.save(supplier);
        return mapSupplierToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacySupplierDto> getPharmacySuppliers() {
        UUID tenantId = resolveTenantId();
        return pharmacySupplierRepository.findByTenantId(tenantId).stream()
                .map(this::mapSupplierToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto request) {
        UUID tenantId = resolveTenantId();
        String poNum = "PO-" + (System.currentTimeMillis() % 1000000);

        PurchaseOrder po = PurchaseOrder.builder()
                .tenantId(tenantId)
                .poNumber(poNum)
                .supplierId(request.getSupplierId())
                .totalAmount(request.getTotalAmount() != null ? request.getTotalAmount() : BigDecimal.ZERO)
                .status("ORDERED")
                .build();

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        return mapPOToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseOrderDto> getPurchaseOrders(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<PurchaseOrderDto> page = purchaseOrderRepository.findByTenantId(tenantId, pageable).map(this::mapPOToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public PrescriptionFulfillmentDto fulfillPrescription(PrescriptionFulfillmentDto request) {
        UUID tenantId = resolveTenantId();
        String fulNum = "FUL-" + (System.currentTimeMillis() % 1000000);

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "id", request.getMedicineId()));

        int qty = request.getQuantityDispensed() != null ? request.getQuantityDispensed() : 1;
        BigDecimal unitPrice = medicine.getUnitPrice() != null ? medicine.getUnitPrice() : BigDecimal.TEN;
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(qty));

        int newStock = Math.max(0, medicine.getStockQuantity() - qty);
        medicine.setStockQuantity(newStock);
        medicineRepository.save(medicine);

        StockMovement movement = StockMovement.builder()
                .tenantId(tenantId)
                .medicineId(medicine.getId())
                .movementType("DISPENSE")
                .quantity(qty)
                .reason("Prescription fulfillment " + fulNum)
                .build();
        stockMovementRepository.save(movement);

        PrescriptionFulfillment fulfillment = PrescriptionFulfillment.builder()
                .tenantId(tenantId)
                .fulfillmentNumber(fulNum)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .medicineId(medicine.getId())
                .quantityDispensed(qty)
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .status("DISPENSED")
                .dispensedAt(ZonedDateTime.now())
                .build();

        PrescriptionFulfillment saved = prescriptionFulfillmentRepository.save(fulfillment);
        return mapFulfillmentToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PrescriptionFulfillmentDto> getPrescriptionFulfillments(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<PrescriptionFulfillmentDto> page = prescriptionFulfillmentRepository.findByTenantId(tenantId, pageable).map(this::mapFulfillmentToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public StockMovementDto logStockMovement(StockMovementDto request) {
        UUID tenantId = resolveTenantId();
        StockMovement movement = StockMovement.builder()
                .tenantId(tenantId)
                .medicineId(request.getMedicineId())
                .movementType(request.getMovementType())
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .build();

        StockMovement saved = stockMovementRepository.save(movement);
        return mapMovementToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StockMovementDto> getStockMovements(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<StockMovementDto> page = stockMovementRepository.findByTenantId(tenantId, pageable).map(this::mapMovementToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public RadiologyRequestDto requestRadiology(RadiologyRequestDto request) {
        UUID tenantId = resolveTenantId();
        String radNum = "RAD-" + (System.currentTimeMillis() % 1000000);

        RadiologyRequest req = RadiologyRequest.builder()
                .tenantId(tenantId)
                .requestNumber(radNum)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .modality(request.getModality())
                .bodyPart(request.getBodyPart())
                .status("COMPLETED")
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl() : "https://images.unsplash.com/photo-1516549655169-df83a0774514")
                .radiologistReport(request.getRadiologistReport() != null ? request.getRadiologistReport() : "Radiology scan completed. Normal diagnostic findings.")
                .build();

        RadiologyRequest saved = radiologyRequestRepository.save(req);
        return mapRadiologyToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RadiologyRequestDto> getRadiologyRequests(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<RadiologyRequestDto> page = radiologyRequestRepository.findByTenantId(tenantId, pageable).map(this::mapRadiologyToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public WardDto createWard(WardDto request) {
        UUID tenantId = resolveTenantId();
        Ward ward = Ward.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .type(request.getType())
                .totalBeds(request.getTotalBeds() != null ? request.getTotalBeds() : 10)
                .availableBeds(request.getAvailableBeds() != null ? request.getAvailableBeds() : 10)
                .build();

        Ward saved = wardRepository.save(ward);
        return mapWardToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WardDto> getWards() {
        UUID tenantId = resolveTenantId();
        return wardRepository.findByTenantId(tenantId).stream().map(this::mapWardToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BedDto allocateBed(BedDto request) {
        UUID tenantId = resolveTenantId();
        Bed bed = Bed.builder()
                .tenantId(tenantId)
                .wardId(request.getWardId())
                .bedNumber(request.getBedNumber())
                .bedType(request.getBedType() != null ? request.getBedType() : "STANDARD")
                .status(request.getPatientId() != null ? "OCCUPIED" : "AVAILABLE")
                .patientId(request.getPatientId())
                .dailyCharge(request.getDailyCharge() != null ? request.getDailyCharge() : new BigDecimal("100.00"))
                .build();

        Bed saved = bedRepository.save(bed);
        return mapBedToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BedDto> getBeds(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<BedDto> page = bedRepository.findByTenantId(tenantId, pageable).map(this::mapBedToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public AuditLogDto logAudit(AuditLogDto request) {
        UUID tenantId = resolveTenantId();
        HospitalAuditLog log = HospitalAuditLog.builder()
                .tenantId(tenantId)
                .userId(request.getUserId())
                .action(request.getAction())
                .resource(request.getResource())
                .details(request.getDetails())
                .ipAddress(request.getIpAddress() != null ? request.getIpAddress() : "127.0.0.1")
                .timestamp(ZonedDateTime.now())
                .build();

        HospitalAuditLog saved = hospitalAuditLogRepository.save(log);
        return mapAuditToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogDto> getAuditLogs(Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<AuditLogDto> page = hospitalAuditLogRepository.findByTenantId(tenantId, pageable).map(this::mapAuditToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public AiClinicalCopilotDto generateAiSummary(AiClinicalCopilotDto request) {
        UUID tenantId = resolveTenantId();

        String notes = request.getAiGeneratedNotes() != null && !request.getAiGeneratedNotes().isBlank()
                ? request.getAiGeneratedNotes()
                : "[AI-GENERATED CLINICAL SUMMARY - REQUIRES HUMAN REVIEW]\nPatient clinical notes summarized automatically based on vital signs and historical EHR entries. Advisory only.";

        AiClinicalCopilot copilot = AiClinicalCopilot.builder()
                .tenantId(tenantId)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .summaryType(request.getSummaryType())
                .aiGeneratedNotes(notes)
                .isReviewedByHuman(false)
                .build();

        AiClinicalCopilot saved = aiClinicalCopilotRepository.save(copilot);
        return mapAiCopilotToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AiClinicalCopilotDto> getAiSummaries(UUID patientId, Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<AiClinicalCopilotDto> page = aiClinicalCopilotRepository.findByTenantIdAndPatientId(tenantId, patientId, pageable)
                .map(this::mapAiCopilotToDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public EhrRecordDto saveEhrRecord(EhrRecordDto request) {
        UUID tenantId = resolveTenantId();

        EhrRecord record = EhrRecord.builder()
                .tenantId(tenantId)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentId(request.getAppointmentId())
                .medicalHistory(request.getMedicalHistory())
                .diagnoses(request.getDiagnoses())
                .allergies(request.getAllergies())
                .vitalsJson(request.getVitalsJson())
                .doctorNotes(request.getDoctorNotes())
                .soapNotes(request.getSoapNotes())
                .immunizations(request.getImmunizations())
                .surgeryHistory(request.getSurgeryHistory())
                .familyHistory(request.getFamilyHistory())
                .attachmentsJson(request.getAttachmentsJson())
                .build();

        EhrRecord saved = ehrRecordRepository.save(record);
        return mapEhrToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<EhrRecordDto> getPatientEhrRecords(UUID patientId, Pageable pageable) {
        UUID tenantId = resolveTenantId();
        Page<EhrRecordDto> page = ehrRecordRepository.findByTenantIdAndPatientId(tenantId, patientId, pageable)
                .map(this::mapEhrToDto);
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
                .specializationId(d.getSpecializationId())
                .specialization(d.getSpecialization())
                .qualification(d.getQualification())
                .experienceYears(d.getExperienceYears())
                .contactNumber(d.getContactNumber())
                .profilePhotoUrl(d.getProfilePhotoUrl())
                .employmentStatus(d.getEmploymentStatus())
                .consultationFee(d.getConsultationFee())
                .licenseNumber(d.getLicenseNumber())
                .isAvailable(d.getIsAvailable())
                .build();
    }

    private SpecializationDto mapSpecializationToDto(Specialization s) {
        return SpecializationDto.builder()
                .id(s.getId())
                .name(s.getName())
                .code(s.getCode())
                .description(s.getDescription())
                .build();
    }

    private ShiftDto mapShiftToDto(Shift s) {
        return ShiftDto.builder()
                .id(s.getId())
                .name(s.getName())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .workingDays(s.getWorkingDays())
                .departmentId(s.getDepartmentId())
                .status(s.getStatus())
                .build();
    }

    private DoctorAvailabilityDto mapDoctorAvailabilityToDto(DoctorAvailability a) {
        return DoctorAvailabilityDto.builder()
                .id(a.getId())
                .doctorId(a.getDoctorId())
                .dayOfWeek(a.getDayOfWeek())
                .startTime(a.getStartTime())
                .endTime(a.getEndTime())
                .slotDurationMinutes(a.getSlotDurationMinutes())
                .isActive(a.getIsActive())
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
                .cancellationReason(a.getCancellationReason())
                .rescheduledFromId(a.getRescheduledFromId())
                .reminderSentEmail(a.getReminderSentEmail())
                .reminderSentSms(a.getReminderSentSms())
                .build();
    }

    private AppointmentWaitingListDto mapWaitingToDto(AppointmentWaitingList w) {
        return AppointmentWaitingListDto.builder()
                .id(w.getId())
                .patientId(w.getPatientId())
                .doctorId(w.getDoctorId())
                .requestedDate(w.getRequestedDate())
                .preferredTimeSlot(w.getPreferredTimeSlot())
                .priorityNotes(w.getPriorityNotes())
                .status(w.getStatus())
                .build();
    }

    private ReminderLogDto mapReminderToDto(ReminderLog r) {
        return ReminderLogDto.builder()
                .id(r.getId())
                .appointmentId(r.getAppointmentId())
                .channel(r.getChannel())
                .recipient(r.getRecipient())
                .message(r.getMessage())
                .status(r.getStatus())
                .sentAt(r.getSentAt())
                .build();
    }

    private InvoiceDto mapInvoiceToDto(Invoice i) {
        List<InvoiceItemDto> itemDtos = i.getItems() != null 
                ? i.getItems().stream().map(item -> InvoiceItemDto.builder()
                        .id(item.getId())
                        .description(item.getDescription())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build()).collect(Collectors.toList())
                : new ArrayList<>();

        return InvoiceDto.builder()
                .id(i.getId())
                .invoiceNumber(i.getInvoiceNumber())
                .patientId(i.getPatientId())
                .appointmentId(i.getAppointmentId())
                .subtotal(i.getSubtotal())
                .taxAmount(i.getTaxAmount())
                .discountAmount(i.getDiscountAmount())
                .totalAmount(i.getTotalAmount())
                .status(i.getStatus())
                .dueDate(i.getDueDate())
                .items(itemDtos)
                .build();
    }

    private PaymentDto mapPaymentToDto(Payment p) {
        return PaymentDto.builder()
                .id(p.getId())
                .invoiceId(p.getInvoiceId())
                .paymentNumber(p.getPaymentNumber())
                .amount(p.getAmount())
                .paymentMethod(p.getPaymentMethod())
                .transactionReference(p.getTransactionReference())
                .status(p.getStatus())
                .paymentDate(p.getPaymentDate())
                .build();
    }

    private InsuranceProviderDto mapProviderToDto(InsuranceProvider p) {
        return InsuranceProviderDto.builder()
                .id(p.getId())
                .name(p.getName())
                .code(p.getCode())
                .contactPhone(p.getContactPhone())
                .email(p.getEmail())
                .address(p.getAddress())
                .build();
    }

    private InsuranceClaimDto mapClaimToDto(InsuranceClaim c) {
        return InsuranceClaimDto.builder()
                .id(c.getId())
                .claimNumber(c.getClaimNumber())
                .patientId(c.getPatientId())
                .insuranceProviderId(c.getInsuranceProviderId())
                .invoiceId(c.getInvoiceId())
                .claimAmount(c.getClaimAmount())
                .approvedAmount(c.getApprovedAmount())
                .status(c.getStatus())
                .notes(c.getNotes())
                .build();
    }

    private LabTestCatalogDto mapLabTestToDto(LabTestCatalog t) {
        return LabTestCatalogDto.builder()
                .id(t.getId())
                .name(t.getName())
                .code(t.getCode())
                .category(t.getCategory())
                .price(t.getPrice())
                .sampleType(t.getSampleType())
                .normalRange(t.getNormalRange())
                .unit(t.getUnit())
                .build();
    }

    private LabOrderDto mapLabOrderToDto(LabOrder o) {
        return LabOrderDto.builder()
                .id(o.getId())
                .orderNumber(o.getOrderNumber())
                .patientId(o.getPatientId())
                .doctorId(o.getDoctorId())
                .appointmentId(o.getAppointmentId())
                .status(o.getStatus())
                .totalAmount(o.getTotalAmount())
                .build();
    }

    private LabSampleDto mapLabSampleToDto(LabSample s) {
        return LabSampleDto.builder()
                .id(s.getId())
                .sampleCode(s.getSampleCode())
                .labOrderId(s.getLabOrderId())
                .specimenType(s.getSpecimenType())
                .status(s.getStatus())
                .collectedAt(s.getCollectedAt())
                .receivedAt(s.getReceivedAt())
                .build();
    }

    private LabTestResultDto mapLabResultToDto(LabTestResult r) {
        return LabTestResultDto.builder()
                .id(r.getId())
                .labOrderId(r.getLabOrderId())
                .testCatalogId(r.getTestCatalogId())
                .resultValue(r.getResultValue())
                .normalRange(r.getNormalRange())
                .unit(r.getUnit())
                .isCritical(r.getIsCritical())
                .status(r.getStatus())
                .pathologistNotes(r.getPathologistNotes())
                .approvedAt(r.getApprovedAt())
                .build();
    }

    private PharmacySupplierDto mapSupplierToDto(PharmacySupplier s) {
        return PharmacySupplierDto.builder()
                .id(s.getId())
                .name(s.getName())
                .code(s.getCode())
                .contactPhone(s.getContactPhone())
                .email(s.getEmail())
                .address(s.getAddress())
                .build();
    }

    private PurchaseOrderDto mapPOToDto(PurchaseOrder p) {
        return PurchaseOrderDto.builder()
                .id(p.getId())
                .poNumber(p.getPoNumber())
                .supplierId(p.getSupplierId())
                .totalAmount(p.getTotalAmount())
                .status(p.getStatus())
                .orderDate(p.getOrderDate())
                .build();
    }

    private PrescriptionFulfillmentDto mapFulfillmentToDto(PrescriptionFulfillment f) {
        return PrescriptionFulfillmentDto.builder()
                .id(f.getId())
                .fulfillmentNumber(f.getFulfillmentNumber())
                .patientId(f.getPatientId())
                .doctorId(f.getDoctorId())
                .medicineId(f.getMedicineId())
                .quantityDispensed(f.getQuantityDispensed())
                .unitPrice(f.getUnitPrice())
                .totalPrice(f.getTotalPrice())
                .status(f.getStatus())
                .dispensedAt(f.getDispensedAt())
                .build();
    }

    private StockMovementDto mapMovementToDto(StockMovement m) {
        return StockMovementDto.builder()
                .id(m.getId())
                .medicineId(m.getMedicineId())
                .movementType(m.getMovementType())
                .quantity(m.getQuantity())
                .reason(m.getReason())
                .build();
    }

    private RadiologyRequestDto mapRadiologyToDto(RadiologyRequest r) {
        return RadiologyRequestDto.builder()
                .id(r.getId())
                .requestNumber(r.getRequestNumber())
                .patientId(r.getPatientId())
                .doctorId(r.getDoctorId())
                .modality(r.getModality())
                .bodyPart(r.getBodyPart())
                .status(r.getStatus())
                .imageUrl(r.getImageUrl())
                .radiologistReport(r.getRadiologistReport())
                .build();
    }

    private WardDto mapWardToDto(Ward w) {
        return WardDto.builder()
                .id(w.getId())
                .name(w.getName())
                .type(w.getType())
                .totalBeds(w.getTotalBeds())
                .availableBeds(w.getAvailableBeds())
                .build();
    }

    private BedDto mapBedToDto(Bed b) {
        return BedDto.builder()
                .id(b.getId())
                .wardId(b.getWardId())
                .bedNumber(b.getBedNumber())
                .bedType(b.getBedType())
                .status(b.getStatus())
                .patientId(b.getPatientId())
                .dailyCharge(b.getDailyCharge())
                .build();
    }

    private AuditLogDto mapAuditToDto(HospitalAuditLog a) {
        return AuditLogDto.builder()
                .id(a.getId())
                .userId(a.getUserId())
                .action(a.getAction())
                .resource(a.getResource())
                .details(a.getDetails())
                .ipAddress(a.getIpAddress())
                .timestamp(a.getTimestamp())
                .build();
    }

    private AiClinicalCopilotDto mapAiCopilotToDto(AiClinicalCopilot c) {
        return AiClinicalCopilotDto.builder()
                .id(c.getId())
                .patientId(c.getPatientId())
                .doctorId(c.getDoctorId())
                .summaryType(c.getSummaryType())
                .aiGeneratedNotes(c.getAiGeneratedNotes())
                .isReviewedByHuman(c.getIsReviewedByHuman())
                .build();
    }

    private EhrRecordDto mapEhrToDto(EhrRecord e) {
        return EhrRecordDto.builder()
                .id(e.getId())
                .patientId(e.getPatientId())
                .doctorId(e.getDoctorId())
                .appointmentId(e.getAppointmentId())
                .medicalHistory(e.getMedicalHistory())
                .diagnoses(e.getDiagnoses())
                .allergies(e.getAllergies())
                .vitalsJson(e.getVitalsJson())
                .doctorNotes(e.getDoctorNotes())
                .soapNotes(e.getSoapNotes())
                .immunizations(e.getImmunizations())
                .surgeryHistory(e.getSurgeryHistory())
                .familyHistory(e.getFamilyHistory())
                .attachmentsJson(e.getAttachmentsJson())
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
