-- =============================================================================
-- V8: LABORATORY INFORMATION SYSTEM (LIS) SCHEMA
-- =============================================================================

-- 1. LAB TEST CATALOG TABLE
CREATE TABLE IF NOT EXISTS lab_test_catalog (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(50) NOT NULL,
    category VARCHAR(100) NOT NULL, -- Hematology, Biochemistry, Microbiology, Pathology, Serology
    price NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    sample_type VARCHAR(50) NOT NULL, -- Blood, Urine, Stool, Swab, Tissue
    normal_range VARCHAR(100),
    unit VARCHAR(30),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_lab_test_code UNIQUE (tenant_id, code)
);

CREATE INDEX idx_lab_catalog_tenant ON lab_test_catalog(tenant_id);

-- 2. LAB ORDERS TABLE
CREATE TABLE IF NOT EXISTS lab_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    order_number VARCHAR(50) NOT NULL,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id UUID REFERENCES doctors(id) ON DELETE SET NULL,
    appointment_id UUID REFERENCES appointments(id) ON DELETE SET NULL,
    status VARCHAR(30) DEFAULT 'ORDERED', -- ORDERED, SAMPLE_COLLECTED, IN_PROGRESS, COMPLETED, CANCELLED
    total_amount NUMERIC(12,2) DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_lab_order_number UNIQUE (tenant_id, order_number)
);

CREATE INDEX idx_lab_orders_tenant ON lab_orders(tenant_id, patient_id);

-- 3. LAB SAMPLES TABLE
CREATE TABLE IF NOT EXISTS lab_samples (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    sample_code VARCHAR(50) NOT NULL,
    lab_order_id UUID NOT NULL REFERENCES lab_orders(id) ON DELETE CASCADE,
    specimen_type VARCHAR(50) NOT NULL,
    status VARCHAR(30) DEFAULT 'COLLECTED', -- COLLECTED, RECEIVED, IN_TESTING, REJECTED
    collected_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    received_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT uk_tenant_sample_code UNIQUE (tenant_id, sample_code)
);

CREATE INDEX idx_lab_samples_tenant ON lab_samples(tenant_id, lab_order_id);

-- 4. LAB TEST RESULTS TABLE
CREATE TABLE IF NOT EXISTS lab_test_results (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    lab_order_id UUID NOT NULL REFERENCES lab_orders(id) ON DELETE CASCADE,
    test_catalog_id UUID NOT NULL REFERENCES lab_test_catalog(id) ON DELETE CASCADE,
    result_value VARCHAR(255) NOT NULL,
    normal_range VARCHAR(100),
    unit VARCHAR(30),
    is_critical BOOLEAN DEFAULT FALSE,
    status VARCHAR(30) DEFAULT 'PENDING_APPROVAL', -- PENDING_APPROVAL, APPROVED, REJECTED
    pathologist_notes TEXT,
    approved_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_lab_results_tenant ON lab_test_results(tenant_id, lab_order_id);
