-- =============================================================================
-- V4: TELEHEALTH VIDEO CONSULTATION & EMERGENCY AMBULANCE MODULES
-- =============================================================================

-- 1. TELEHEALTH SESSIONS
CREATE TABLE IF NOT EXISTS telehealth_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    appointment_id UUID REFERENCES appointments(id) ON DELETE SET NULL,
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    room_id VARCHAR(100) NOT NULL UNIQUE,
    join_token VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'CREATED', -- CREATED, ACTIVE, COMPLETED, ENDED
    scheduled_start TIMESTAMP WITH TIME ZONE NOT NULL,
    actual_end TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_telehealth_tenant ON telehealth_sessions(tenant_id);

-- 2. AMBULANCE FLEET & EMERGENCY DISPATCH
CREATE TABLE IF NOT EXISTS ambulances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    vehicle_number VARCHAR(30) NOT NULL,
    driver_name VARCHAR(100) NOT NULL,
    driver_phone VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, ON_DISPATCH, MAINTENANCE
    current_location VARCHAR(150),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS emergency_admissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    patient_id UUID REFERENCES patients(id) ON DELETE SET NULL,
    triage_level VARCHAR(30) NOT NULL, -- CRITICAL, SEVERE, MODERATE, LOW
    symptoms_summary TEXT NOT NULL,
    ambulance_id UUID REFERENCES ambulances(id) ON DELETE SET NULL,
    assigned_doctor_id UUID REFERENCES doctors(id) ON DELETE SET NULL,
    admission_status VARCHAR(30) NOT NULL DEFAULT 'ADMITTED',
    admitted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_emergency_tenant ON emergency_admissions(tenant_id);
