import { NavItem } from './types';

export const API_BASE_URL = '/api/v1';
export const DEFAULT_PAGE_SIZE = 20;
export const DEBOUNCE_DELAY = 300;
export const TOAST_DURATION = 4000;

export const NAV_ITEMS: NavItem[] = [
  { id: 'dashboard', label: 'Dashboard', icon: '📊' },
  { id: 'patients', label: 'Patients', icon: '👤' },
  { id: 'doctors', label: 'Doctors', icon: '🩺' },
  { id: 'appointments', label: 'Appointments', icon: '📅' },
  { id: 'ehr', label: 'EHR', icon: '📋' },
  { id: 'laboratory', label: 'Laboratory', icon: '🔬' },
  { id: 'pharmacy', label: 'Pharmacy', icon: '💊' },
  { id: 'radiology', label: 'Radiology', icon: '📡' },
  { id: 'billing', label: 'Billing', icon: '💳' },
  { id: 'wards', label: 'Wards', icon: '🛏️' },
  { id: 'audit', label: 'Audit & AI', icon: '🔒' }
];

export const APPOINTMENT_STATUSES: Record<string, string> = {
  SCHEDULED: '#3b82f6', // blue
  CONFIRMED: '#10b981', // green
  COMPLETED: '#059669', // dark green
  CANCELLED: '#ef4444', // red
  NO_SHOW: '#f59e0b', // yellow
};

export const BLOOD_GROUPS = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

export const PAYMENT_METHODS = ['CASH', 'CREDIT_CARD', 'STRIPE', 'INSURANCE'];

export const INVOICE_STATUSES: Record<string, string> = {
  DRAFT: '#6b7280', // gray
  SENT: '#3b82f6', // blue
  PAID: '#10b981', // green
  OVERDUE: '#ef4444', // red
  CANCELLED: '#9ca3af', // light gray
};

export const CLAIM_STATUSES: Record<string, string> = {
  SUBMITTED: '#3b82f6', // blue
  UNDER_REVIEW: '#f59e0b', // yellow
  APPROVED: '#10b981', // green
  REJECTED: '#ef4444', // red
  PAID: '#059669', // dark green
};

export const LAB_STATUSES: Record<string, string> = {
  ORDERED: '#6b7280', // gray
  COLLECTED: '#3b82f6', // blue
  IN_PROGRESS: '#f59e0b', // yellow
  COMPLETED: '#10b981', // green
  APPROVED: '#059669', // dark green
};

export const BED_STATUSES: Record<string, string> = {
  AVAILABLE: '#10b981', // green
  OCCUPIED: '#ef4444', // red
  MAINTENANCE: '#f59e0b', // yellow
  RESERVED: '#3b82f6', // blue
};

export const RADIOLOGY_MODALITIES = ['XRAY', 'CT', 'MRI', 'ULTRASOUND'];

export const WARD_TYPES = ['GENERAL', 'ICU', 'PRIVATE', 'SURGICAL', 'MATERNITY'];

export const SPECIALIZATIONS = [
  'Cardiology',
  'Neurology',
  'Orthopedics',
  'Pediatrics',
  'General Surgery',
  'Internal Medicine',
  'Dermatology',
  'Psychiatry',
  'Oncology',
  'Radiology',
  'Gynecology'
];
