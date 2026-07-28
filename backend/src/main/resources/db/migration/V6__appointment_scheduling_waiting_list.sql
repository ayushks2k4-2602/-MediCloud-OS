-- =============================================================================
-- V6: ADVANCED APPOINTMENT SCHEDULING, DOCTOR AVAILABILITY & WAITING LIST
-- =============================================================================

-- 1. EXTEND APPOINTMENTS TABLE
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS cancellation_reason TEXT;
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS rescheduled_from_id UUID REFERENCES appointments(id) ON DELETE SET NULL;
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS reminder_sent_email BOOLEAN DEFAULT FALSE;
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS reminder_sent_sms BOOLEAN DEFAULT FALSE;

-- 2. DOCTOR AVAILABILITY TABLE
CREATE TABLE IF NOT EXISTS doctor_availabilities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    day_of_week VARCHAR(20) NOT NULL, -- MONDAY, TUESDAY, etc.
    start_time VARCHAR(20) NOT NULL,   -- e.g. "09:00 AM"
    end_time VARCHAR(20) NOT NULL,     -- e.g. "05:00 PM"
    slot_duration_minutes INT DEFAULT 30,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_doctor_day UNIQUE (tenant_id, doctor_id, day_of_week)
);

CREATE INDEX idx_doctor_availabilities_tenant ON doctor_availabilities(tenant_id, doctor_id);

-- 3. APPOINTMENT WAITING LIST TABLE
CREATE TABLE IF NOT EXISTS appointment_waiting_list (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    requested_date DATE NOT NULL,
    preferred_time_slot VARCHAR(30),
    priority_notes TEXT,
    status VARCHAR(30) DEFAULT 'WAITING', -- WAITING, NOTIFIED, CONVERTED, CANCELLED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_waiting_list_tenant ON appointment_waiting_list(tenant_id, doctor_id, requested_date);

-- 4. REMINDER LOGS TABLE
CREATE TABLE IF NOT EXISTS reminder_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    appointment_id UUID REFERENCES appointments(id) ON DELETE CASCADE,
    channel VARCHAR(20) NOT NULL, -- EMAIL, SMS
    recipient VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(30) DEFAULT 'SENT', -- SENT, FAILED
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_reminder_logs_tenant ON reminder_logs(tenant_id);
