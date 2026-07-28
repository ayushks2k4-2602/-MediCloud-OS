export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface User {
  id: string;
  email: string;
  fullName: string;
  role: string;
  tenantId: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  user: User;
}

export interface AuthState {
  token: string | null;
  user: User | null;
  tenantId: string | null;
  isAuthenticated: boolean;
}

export interface NavItem {
  id: string;
  label: string;
  icon: string;
}

export interface ToastMessage {
  id: string;
  type: 'success' | 'error' | 'info' | 'warning';
  message: string;
  duration?: number;
}

// Entities

export interface Patient {
  id: string;
  patientCode: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  dateOfBirth: string;
  gender: string;
  bloodGroup: string;
  address: string;
  emergencyContact: string;
  insuranceProvider: string;
  insurancePolicyNumber: string;
  tenantId: string;
  createdAt: string;
  updatedAt: string;
}

export interface Doctor {
  id: string;
  specialization: string;
  qualification: string;
  consultationFee: number;
  licenseNumber: string;
  isAvailable: boolean;
  tenantId: string;
}

export interface DoctorAvailability {
  id: string;
  doctorId: string;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
  maxAppointments: number;
  tenantId: string;
}

export interface Specialization {
  id: string;
  name: string;
  description: string;
  tenantId: string;
}

export interface Shift {
  id: string;
  name: string;
  startTime: string;
  endTime: string;
  tenantId: string;
}

export interface Appointment {
  id: string;
  patientId: string;
  doctorId: string;
  appointmentDate: string;
  startTime: string;
  endTime: string;
  status: 'SCHEDULED' | 'CONFIRMED' | 'COMPLETED' | 'CANCELLED' | 'NO_SHOW';
  notes: string;
  rescheduledFromId?: string;
  tenantId: string;
}

export interface AppointmentWaitingList {
  id: string;
  patientId: string;
  doctorId: string;
  preferredDate: string;
  priority: string;
  status: string;
  tenantId: string;
}

export interface ReminderLog {
  id: string;
  appointmentId: string;
  channel: 'EMAIL' | 'SMS';
  status: string;
  sentAt: string;
  tenantId: string;
}

export interface Invoice {
  id: string;
  invoiceNumber: string;
  patientId: string;
  totalAmount: number;
  tax: number;
  discount: number;
  netAmount: number;
  status: 'DRAFT' | 'SENT' | 'PAID' | 'OVERDUE' | 'CANCELLED';
  items: InvoiceItem[];
  tenantId: string;
}

export interface InvoiceItem {
  id: string;
  invoiceId: string;
  description: string;
  quantity: number;
  unitPrice: number;
  amount: number;
}

export interface Payment {
  id: string;
  invoiceId: string;
  amount: number;
  paymentMethod: 'CASH' | 'CREDIT_CARD' | 'STRIPE' | 'INSURANCE';
  transactionReference: string;
  status: 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED';
  tenantId: string;
}

export interface InsuranceProvider {
  id: string;
  name: string;
  contactEmail: string;
  contactPhone: string;
  address: string;
  tenantId: string;
}

export interface InsuranceClaim {
  id: string;
  claimNumber: string;
  patientId: string;
  invoiceId: string;
  providerId: string;
  claimAmount: number;
  approvedAmount: number;
  status: 'SUBMITTED' | 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED' | 'PAID';
  rejectionReason: string;
  tenantId: string;
}

export interface LabTestCatalog {
  id: string;
  testCode: string;
  testName: string;
  department: string;
  sampleType: string;
  normalRange: string;
  unit: string;
  price: number;
  tenantId: string;
}

export interface LabOrder {
  id: string;
  orderCode: string;
  patientId: string;
  doctorId: string;
  tests: string[];
  status: 'ORDERED' | 'COLLECTED' | 'IN_PROGRESS' | 'COMPLETED' | 'APPROVED';
  tenantId: string;
}

export interface LabSample {
  id: string;
  sampleCode: string;
  labOrderId: string;
  sampleType: string;
  collectedAt: string;
  status: string;
  tenantId: string;
}

export interface LabTestResult {
  id: string;
  labOrderId: string;
  labSampleId: string;
  testCatalogId: string;
  value: string;
  unit: string;
  normalRange: string;
  isAbnormal: boolean;
  isCritical: boolean;
  approvedBy: string;
  tenantId: string;
}

export interface PharmacySupplier {
  id: string;
  name: string;
  contactEmail: string;
  contactPhone: string;
  address: string;
  tenantId: string;
}

export interface PurchaseOrder {
  id: string;
  supplierId: string;
  medicineId: string;
  quantity: number;
  unitCost: number;
  totalCost: number;
  status: string;
  tenantId: string;
}

export interface PrescriptionFulfillment {
  id: string;
  patientId: string;
  medicineId: string;
  prescribedBy: string;
  quantity: number;
  dispensedAt: string;
  notes: string;
  tenantId: string;
}

export interface StockMovement {
  id: string;
  medicineId: string;
  movementType: 'PURCHASE' | 'DISPENSE' | 'ADJUSTMENT' | 'RETURN';
  quantity: number;
  reference: string;
  tenantId: string;
}

export interface Medicine {
  id: string;
  name: string;
  genericName: string;
  manufacturer: string;
  category: string;
  dosageForm: string;
  strength: string;
  stockQuantity: number;
  reorderLevel: number;
  unitPrice: number;
  expiryDate: string;
  tenantId: string;
}

export interface RadiologyRequest {
  id: string;
  patientId: string;
  doctorId: string;
  modality: 'XRAY' | 'CT' | 'MRI' | 'ULTRASOUND';
  bodyPart: string;
  clinicalIndication: string;
  status: 'REQUESTED' | 'SCHEDULED' | 'COMPLETED' | 'REPORTED';
  imageUrl: string;
  findings: string;
  reportedBy: string;
  tenantId: string;
}

export interface Ward {
  id: string;
  name: string;
  type: 'GENERAL' | 'ICU' | 'PRIVATE' | 'SURGICAL' | 'MATERNITY';
  capacity: number;
  currentOccupancy: number;
  tenantId: string;
}

export interface Bed {
  id: string;
  bedCode: string;
  wardId: string;
  patientId: string;
  status: 'AVAILABLE' | 'OCCUPIED' | 'MAINTENANCE' | 'RESERVED';
  dailyRate: number;
  tenantId: string;
}

export interface HospitalAuditLog {
  id: string;
  action: string;
  entityType: string;
  entityId: string;
  userId: string;
  userEmail: string;
  ipAddress: string;
  details: string;
  tenantId: string;
  createdAt: string;
}

export interface AiClinicalCopilot {
  id: string;
  patientId: string;
  summaryType: 'SOAP_SUMMARY' | 'VISIT_NOTES' | 'DISCHARGE_SUMMARY';
  content: string;
  generatedAt: string;
  humanReviewed: boolean;
  tenantId: string;
}

export interface EhrRecord {
  id: string;
  patientId: string;
  doctorId: string;
  diagnosis: string;
  vitalBp: string;
  vitalHeartRate: number;
  vitalTemp: number;
  vitalWeight: number;
  doctorNotes: string;
  visitDate: string;
  tenantId: string;
}
