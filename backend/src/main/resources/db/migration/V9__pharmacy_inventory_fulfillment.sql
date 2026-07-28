-- =============================================================================
-- V9: PHARMACY INVENTORY, PURCHASE ORDERS & PRESCRIPTION FULFILLMENT
-- =============================================================================

-- 1. PHARMACY SUPPLIERS TABLE
CREATE TABLE IF NOT EXISTS pharmacy_suppliers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(50) NOT NULL,
    contact_phone VARCHAR(30),
    email VARCHAR(100),
    address TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_supplier_code UNIQUE (tenant_id, code)
);

CREATE INDEX idx_pharmacy_suppliers_tenant ON pharmacy_suppliers(tenant_id);

-- 2. PURCHASE ORDERS TABLE
CREATE TABLE IF NOT EXISTS purchase_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    po_number VARCHAR(50) NOT NULL,
    supplier_id UUID NOT NULL REFERENCES pharmacy_suppliers(id) ON DELETE CASCADE,
    total_amount NUMERIC(12,2) DEFAULT 0.00,
    status VARCHAR(30) DEFAULT 'ORDERED', -- DRAFT, ORDERED, RECEIVED, CANCELLED
    order_date DATE DEFAULT CURRENT_DATE,
    CONSTRAINT uk_tenant_po_number UNIQUE (tenant_id, po_number)
);

CREATE INDEX idx_purchase_orders_tenant ON purchase_orders(tenant_id);

-- 3. PRESCRIPTION FULFILLMENTS TABLE
CREATE TABLE IF NOT EXISTS prescription_fulfillments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    fulfillment_number VARCHAR(50) NOT NULL,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id UUID REFERENCES doctors(id) ON DELETE SET NULL,
    medicine_id UUID NOT NULL REFERENCES medicines(id) ON DELETE CASCADE,
    quantity_dispensed INT NOT NULL DEFAULT 1,
    unit_price NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    total_price NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) DEFAULT 'DISPENSED', -- DISPENSED, CANCELLED
    dispensed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_fulfillment_number UNIQUE (tenant_id, fulfillment_number)
);

CREATE INDEX idx_fulfillments_tenant ON prescription_fulfillments(tenant_id, patient_id);

-- 4. STOCK MOVEMENTS TABLE
CREATE TABLE IF NOT EXISTS stock_movements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    medicine_id UUID NOT NULL REFERENCES medicines(id) ON DELETE CASCADE,
    movement_type VARCHAR(30) NOT NULL, -- PURCHASE, DISPENSE, ADJUSTMENT, RETURN
    quantity INT NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stock_movements_tenant ON stock_movements(tenant_id, medicine_id);
