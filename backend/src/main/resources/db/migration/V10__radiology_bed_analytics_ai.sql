-- =============================================================================
-- V10: RADIOLOGY, BED & WARD MANAGEMENT, AUDIT LOGS, AND AI CLINICAL COPILOT
-- =============================================================================

-- 1. RADIOLOGY REQUESTS TABLE
CREATE TABLE IF NOT EXISTS radiology_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    request_number VARCHAR(50) NOT NULL,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id UUID REFERENCES doctors(id) ON DELETE SET NULL,
    modality VARCHAR(50) NOT NULL, -- X_RAY, CT_SCAN, MRI, ULTRASOUND, PET_SCAN
    body_part VARCHAR(100) NOT NULL,
    status VARCHAR(30) DEFAULT 'REQUESTED', -- REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED
    image_url TEXT,
    radiologist_report TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_rad_number UNIQUE (tenant_id, request_number)
);

CREATE INDEX idx_radiology_requests_tenant ON radiology_requests(tenant_id, patient_id);

-- 2. WARDS TABLE
CREATE TABLE IF NOT EXISTS wards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL, -- GENERAL, ICU, PRIVATE, DELUXE, PEDIATRIC, SURGICAL
    total_beds INT NOT NULL DEFAULT 10,
    available_beds INT NOT NULL DEFAULT 10,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_ward_name UNIQUE (tenant_id, name)
);

CREATE INDEX idx_wards_tenant ON wards(tenant_id);

-- 3. BEDS TABLE
CREATE TABLE IF NOT EXISTS beds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    ward_id UUID NOT NULL REFERENCES wards(id) ON DELETE CASCADE,
    bed_number VARCHAR(30) NOT NULL,
    bed_type VARCHAR(50) DEFAULT 'STANDARD',
    status VARCHAR(30) DEFAULT 'AVAILABLE', -- AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
    patient_id UUID REFERENCES patients(id) ON DELETE SET NULL,
    daily_charge NUMERIC(12,2) DEFAULT 100.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_ward_bed UNIQUE (tenant_id, ward_id, bed_number)
);

CREATE INDEX idx_beds_tenant ON beds(tenant_id, ward_id);

-- 4. AUDIT LOGS TABLE (COMPLIANCE & AUDIT)
CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    user_id UUID,
    action VARCHAR(100) NOT NULL,
    resource VARCHAR(100) NOT NULL,
    details TEXT,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_tenant ON audit_logs(tenant_id, timestamp);

-- 5. AI CLINICAL COPILOT TABLE
CREATE TABLE IF NOT EXISTS ai_clinical_copilots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id UUID REFERENCES doctors(id) ON DELETE SET NULL,
    summary_type VARCHAR(50) NOT NULL, -- SOAP_SUMMARY, VISIT_SUMMARY, DISCHARGE_DRAFT, DIAGNOSTIC_ADVISORY
    ai_generated_notes TEXT NOT NULL,
    is_reviewed_by_human BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ai_copilot_tenant ON ai_clinical_copilots(tenant_id, patient_id);
