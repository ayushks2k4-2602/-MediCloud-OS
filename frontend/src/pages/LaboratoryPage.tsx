import React, { useState, useEffect, useCallback } from 'react';
import { api } from '../api';
import { usePagination } from '../hooks';
import { PageResponse, LabTestCatalog, LabOrder, LabSample, LabTestResult } from '../types';
import Modal from '../components/Modal';
import { DataTable } from '../components/DataTable';
import { Badge } from '../components/Badge';
import { EmptyState } from '../components/EmptyState';

interface Props {
  toast: {
    success: (msg: string) => void;
    error: (msg: string) => void;
  };
}

export default function LaboratoryPage({ toast }: Props) {
  const [activeTab, setActiveTab] = useState<'catalog' | 'orders' | 'samples' | 'results'>('catalog');

  return (
    <div className="page-container animate-fade-in">
      <header className="page-header mb-6">
        <h1 className="page-title mb-2">Laboratory Information System (LIS)</h1>
        <p className="page-subtitle mb-4">Manage lab tests, specimen tracking, orders, and diagnostic results</p>
        <div className="flex border-b gap-6" style={{ borderColor: 'var(--border)' }}>
          {(['catalog', 'orders', 'samples', 'results'] as const).map(tab => (
            <button 
              key={tab}
              className={`pb-2 btn-ghost ${activeTab === tab ? 'text-teal-400 font-bold' : 'text-slate-400'}`}
              style={{
                borderBottom: activeTab === tab ? '2px solid #14b8a6' : 'none',
                color: activeTab === tab ? '#14b8a6' : 'var(--text-muted)'
              }}
              onClick={() => setActiveTab(tab)}
            >
              {tab.charAt(0).toUpperCase() + tab.slice(1)}
            </button>
          ))}
        </div>
      </header>
      
      <div className="tab-content mt-4">
        {activeTab === 'catalog' && <TestCatalogTab toast={toast} />}
        {activeTab === 'orders' && <LabOrdersTab toast={toast} />}
        {activeTab === 'samples' && <SamplesTab toast={toast} />}
        {activeTab === 'results' && <ResultsTab toast={toast} />}
      </div>
    </div>
  );
}

function TestCatalogTab({ toast }: Props) {
  const [tests, setTests] = useState<LabTestCatalog[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [form, setForm] = useState({ testCode: '', testName: '', department: 'Hematology', sampleType: 'Blood', normalRange: '4.0 - 11.0', unit: 'x10^9/L', price: '45.00' });

  const fetchTests = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get<LabTestCatalog[]>('/hospital/lab/tests');
      setTests(res.data || []);
    } catch {
      toast.error('Failed to load lab test catalog');
    } finally {
      setLoading(false);
    }
  }, [toast]);

  useEffect(() => { fetchTests(); }, [fetchTests]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post<LabTestCatalog>('/hospital/lab/tests', { ...form, price: Number(form.price) });
      toast.success('Lab test added to catalog');
      setModalOpen(false);
      fetchTests();
    } catch {
      toast.error('Error adding test to catalog');
    }
  };

  const columns = [
    { key: 'testCode', label: 'Test Code', render: (r: LabTestCatalog) => <span className="font-mono text-teal-600" style={{ color: '#0d9488', fontFamily: 'monospace' }}>{r.testCode}</span> },
    { key: 'testName', label: 'Test Name' },
    { key: 'department', label: 'Department' },
    { key: 'sampleType', label: 'Specimen' },
    { key: 'normalRange', label: 'Normal Range' },
    { key: 'unit', label: 'Unit' },
    { key: 'price', label: 'Price', render: (r: LabTestCatalog) => `$${r.price}` }
  ];

  return (
    <div>
      <div className="flex flex-between mb-4">
        <h2 className="section-title">Test Catalog</h2>
        <button className="btn btn-primary" onClick={() => setModalOpen(true)}>+ Add Lab Test</button>
      </div>
      {loading ? <div>Loading tests...</div> : tests.length === 0 ? <EmptyState title="No lab tests in catalog" /> : <DataTable columns={columns} data={tests} />}
      
      <Modal isOpen={modalOpen} title="Add Lab Test" onClose={() => setModalOpen(false)}>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Test Code</label><input className="input w-full" value={form.testCode} onChange={e => setForm({...form, testCode: e.target.value})} required /></div>
          <div className="form-group"><label className="label">Test Name</label><input className="input w-full" value={form.testName} onChange={e => setForm({...form, testName: e.target.value})} required /></div>
          <div className="form-group"><label className="label">Department</label><input className="input w-full" value={form.department} onChange={e => setForm({...form, department: e.target.value})} required /></div>
          <div className="form-group"><label className="label">Sample Type</label><input className="input w-full" value={form.sampleType} onChange={e => setForm({...form, sampleType: e.target.value})} required /></div>
          <div className="form-group"><label className="label">Normal Range</label><input className="input w-full" value={form.normalRange} onChange={e => setForm({...form, normalRange: e.target.value})} required /></div>
          <div className="form-group"><label className="label">Unit</label><input className="input w-full" value={form.unit} onChange={e => setForm({...form, unit: e.target.value})} required /></div>
          <div className="form-group"><label className="label">Price ($)</label><input type="number" step="0.01" className="input w-full" value={form.price} onChange={e => setForm({...form, price: e.target.value})} required /></div>
          <button type="submit" className="btn btn-primary">Save Test</button>
        </form>
      </Modal>
    </div>
  );
}

function LabOrdersTab({ toast }: Props) {
  const pagination = usePagination(20);
  const [orders, setOrders] = useState<LabOrder[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchOrders = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get<PageResponse<LabOrder>>(`/hospital/lab/orders?page=${pagination.page}&size=${pagination.size}`);
      setOrders(res.data.content || []);
      pagination.updateFromResponse({
        totalPages: res.data.totalPages || 1,
        totalElements: res.data.totalElements || 0
      });
    } catch {
      toast.error('Failed to load lab orders');
    } finally {
      setLoading(false);
    }
  }, [pagination.page, pagination.size, toast]);

  useEffect(() => { fetchOrders(); }, [fetchOrders]);

  const columns = [
    { key: 'orderCode', label: 'Order Code', render: (r: LabOrder) => <span className="font-mono text-teal-600" style={{ color: '#0d9488', fontFamily: 'monospace' }}>{r.orderCode}</span> },
    { key: 'patientId', label: 'Patient ID', render: (r: LabOrder) => r.patientId?.substring(0, 8) + '...' },
    { key: 'doctorId', label: 'Doctor ID', render: (r: LabOrder) => r.doctorId?.substring(0, 8) + '...' },
    { key: 'status', label: 'Status', render: (r: LabOrder) => <Badge variant="info" label={r.status} /> }
  ];

  return (
    <div>
      <h2 className="section-title mb-4">Lab Orders</h2>
      {loading ? <div>Loading orders...</div> : orders.length === 0 ? <EmptyState title="No lab orders" /> : (
        <DataTable 
          columns={columns} 
          data={orders}
          page={pagination.page + 1}
          totalPages={pagination.totalPages}
          onPageChange={(p) => pagination.goToPage(p - 1)}
        />
      )}
    </div>
  );
}

function SamplesTab({ toast }: Props) {
  const pagination = usePagination(20);
  const [samples, setSamples] = useState<LabSample[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchSamples = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get<PageResponse<LabSample>>(`/hospital/lab/samples?page=${pagination.page}&size=${pagination.size}`);
      setSamples(res.data.content || []);
      pagination.updateFromResponse({
        totalPages: res.data.totalPages || 1,
        totalElements: res.data.totalElements || 0
      });
    } catch {
      toast.error('Failed to load lab samples');
    } finally {
      setLoading(false);
    }
  }, [pagination.page, pagination.size, toast]);

  useEffect(() => { fetchSamples(); }, [fetchSamples]);

  const columns = [
    { key: 'sampleCode', label: 'Sample Barcode', render: (r: LabSample) => <span className="font-mono text-teal-600" style={{ color: '#0d9488', fontFamily: 'monospace' }}>{r.sampleCode}</span> },
    { key: 'labOrderId', label: 'Order ID', render: (r: LabSample) => r.labOrderId?.substring(0, 8) + '...' },
    { key: 'sampleType', label: 'Specimen Type' },
    { key: 'status', label: 'Status', render: (r: LabSample) => <Badge variant="success" label={r.status || 'COLLECTED'} /> }
  ];

  return (
    <div>
      <h2 className="section-title mb-4">Collected Specimen Samples</h2>
      {loading ? <div>Loading samples...</div> : samples.length === 0 ? <EmptyState title="No specimen samples collected" /> : (
        <DataTable 
          columns={columns} 
          data={samples}
          page={pagination.page + 1}
          totalPages={pagination.totalPages}
          onPageChange={(p) => pagination.goToPage(p - 1)}
        />
      )}
    </div>
  );
}

function ResultsTab({ toast }: Props) {
  const pagination = usePagination(20);
  const [results, setResults] = useState<LabTestResult[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchResults = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get<PageResponse<LabTestResult>>(`/hospital/lab/results?page=${pagination.page}&size=${pagination.size}`);
      setResults(res.data.content || []);
      pagination.updateFromResponse({
        totalPages: res.data.totalPages || 1,
        totalElements: res.data.totalElements || 0
      });
    } catch {
      toast.error('Failed to load lab results');
    } finally {
      setLoading(false);
    }
  }, [pagination.page, pagination.size, toast]);

  useEffect(() => { fetchResults(); }, [fetchResults]);

  const columns = [
    { key: 'labOrderId', label: 'Order ID', render: (r: LabTestResult) => r.labOrderId?.substring(0, 8) + '...' },
    { key: 'value', label: 'Value & Unit', render: (r: LabTestResult) => `${r.value} ${r.unit || ''}` },
    { key: 'normalRange', label: 'Normal Range' },
    { key: 'isAbnormal', label: 'Abnormal Flag', render: (r: LabTestResult) => r.isAbnormal ? <Badge variant="danger" label="ABNORMAL" /> : <Badge variant="neutral" label="NORMAL" /> },
    { key: 'approvedBy', label: 'Approved By' }
  ];

  return (
    <div>
      <h2 className="section-title mb-4">Lab Test Diagnostic Results</h2>
      {loading ? <div>Loading results...</div> : results.length === 0 ? <EmptyState title="No test results recorded" /> : (
        <DataTable 
          columns={columns} 
          data={results}
          page={pagination.page + 1}
          totalPages={pagination.totalPages}
          onPageChange={(p) => pagination.goToPage(p - 1)}
        />
      )}
    </div>
  );
}
