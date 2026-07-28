-- =============================================================================
-- V5: SPECIALIZATIONS, ENHANCED DOCTORS, SHIFTS & ADVANCED EHR SCHEMA
-- =============================================================================

-- 1. MEDICAL SPECIALIZATIONS TABLE
CREATE TABLE IF NOT EXISTS specializations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(30) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_specialization_code UNIQUE (tenant_id, code)
);

CREATE INDEX idx_specializations_tenant ON specializations(tenant_id);

-- 2. EXTEND DOCTORS TABLE
ALTER TABLE doctors ADD COLUMN IF NOT EXISTS specialization_id UUID REFERENCES specializations(id) ON DELETE SET NULL;
ALTER TABLE doctors ADD COLUMN IF NOT EXISTS experience_years INT DEFAULT 5;
ALTER TABLE doctors ADD COLUMN IF NOT EXISTS contact_number VARCHAR(30);
ALTER TABLE doctors ADD COLUMN IF NOT EXISTS profile_photo_url VARCHAR(255);
ALTER TABLE doctors ADD COLUMN IF NOT EXISTS employment_status VARCHAR(30) DEFAULT 'FULL_TIME'; -- FULL_TIME, PART_TIME, VISITING, ON_CALL

-- 3. SHIFTS TABLE
CREATE TABLE IF NOT EXISTS shifts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL, -- Morning, Afternoon, Evening, Night, Custom
    start_time VARCHAR(20) NOT NULL, -- e.g. "08:00 AM"
    end_time VARCHAR(20) NOT NULL,   -- e.g. "04:00 PM"
    working_days VARCHAR(100) DEFAULT 'Mon,Tue,Wed,Thu,Fri',
    department_id UUID REFERENCES departments(id) ON DELETE SET NULL,
    status VARCHAR(30) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_shifts_tenant ON shifts(tenant_id);

-- 4. DOCTOR SHIFTS MAPPING TABLE
CREATE TABLE IF NOT EXISTS doctor_shifts (
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    shift_id UUID NOT NULL REFERENCES shifts(id) ON DELETE CASCADE,
    PRIMARY KEY (doctor_id, shift_id)
);

-- 5. COMPREHENSIVE EHR RECORDS TABLE
CREATE TABLE IF NOT EXISTS ehr_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id UUID REFERENCES doctors(id) ON DELETE SET NULL,
    appointment_id UUID REFERENCES appointments(id) ON DELETE SET NULL,
    medical_history TEXT,
    diagnoses TEXT NOT NULL,
    allergies TEXT,
    vitals_json TEXT, -- BP, HR, Temp, Weight, Oxygen Saturation
    doctor_notes TEXT,
    soap_notes TEXT, -- Subjective, Objective, Assessment, Plan
    immunizations TEXT,
    surgery_history TEXT,
    family_history TEXT,
    attachments_json TEXT, -- PDF / Image URLs
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ehr_records_patient ON ehr_records(patient_id);
