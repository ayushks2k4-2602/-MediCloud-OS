import React, { useState, useEffect, useCallback } from 'react';
import { api } from '../api';
import Modal from '../components/Modal';
import { DataTable } from '../components/DataTable';
import { Badge } from '../components/Badge';
import { EmptyState } from '../components/EmptyState';

interface ToastProps {
  toast: {
    success: (msg: string) => void;
    error: (msg: string) => void;
  };
}

export default function RadiologyPage({ toast }: ToastProps) {
  const [scans, setScans] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form, setForm] = useState({ patientId: '', doctorId: '', modality: 'XRAY', bodyPart: '', clinicalIndication: '' });

  const fetchScans = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get<any>('/hospital/radiology?page=0&size=20');
      setScans(res.data.content || res.data);
    } catch (err) {
      setError('Failed to fetch radiology scans');
      toast.error('Error fetching scans');
    } finally {
      setLoading(false);
    }
  }, [toast]);

  useEffect(() => {
    fetchScans();
  }, [fetchScans]);

  const handleRequestScan = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/hospital/radiology', form);
      toast.success('Scan requested successfully');
      setIsModalOpen(false);
      fetchScans();
    } catch (err) {
      toast.error('Failed to request scan');
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'REQUESTED': return <Badge variant="warning" label="REQUESTED" />;
      case 'SCHEDULED': return <Badge variant="info" label="SCHEDULED" />;
      case 'COMPLETED': return <Badge variant="success" label="COMPLETED" />;
      case 'REPORTED': return <Badge variant="success" label="REPORTED" />;
      default: return <Badge variant="neutral" label={status || 'PENDING'} />;
    }
  };

  return (
    <div className="animate-fade-in">
      <header className="page-header flex flex-between">
        <div>
          <h1 className="page-title">Radiology</h1>
          <p className="page-subtitle">Manage radiology scan requests and reports.</p>
        </div>
        <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>+ Request Scan</button>
      </header>

      {error && <div className="badge badge-danger">{error}</div>}

      <div className="card mt-6">
        {loading ? (
          <div>Loading...</div>
        ) : scans.length === 0 ? (
          <EmptyState title="No radiology scans found" />
        ) : (
          <DataTable
            columns={[
              { key: 'patientId', label: 'Patient ID' },
              { key: 'doctorId', label: 'Doctor ID' },
              { key: 'modality', label: 'Modality', render: (row: any) => <Badge variant="info" label={row.modality} /> },
              { key: 'bodyPart', label: 'Body Part' },
              { key: 'clinicalIndication', label: 'Indication' },
              { key: 'status', label: 'Status', render: (row: any) => getStatusBadge(row.status) },
              { key: 'findings', label: 'Findings' }
            ]}
            data={scans}
          />
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Request Scan">
        <form onSubmit={handleRequestScan} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Patient ID</label><input className="input" required value={form.patientId} onChange={e => setForm({...form, patientId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Doctor ID</label><input className="input" required value={form.doctorId} onChange={e => setForm({...form, doctorId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Modality</label>
            <select className="select" required value={form.modality} onChange={e => setForm({...form, modality: e.target.value})}>
              <option value="XRAY">X-Ray</option>
              <option value="CT">CT Scan</option>
              <option value="MRI">MRI</option>
              <option value="ULTRASOUND">Ultrasound</option>
            </select>
          </div>
          <div className="form-group"><label className="label">Body Part</label><input className="input" required value={form.bodyPart} onChange={e => setForm({...form, bodyPart: e.target.value})} /></div>
          <div className="form-group"><label className="label">Clinical Indication</label><textarea className="textarea" required value={form.clinicalIndication} onChange={e => setForm({...form, clinicalIndication: e.target.value})} /></div>
          <button type="submit" className="btn btn-primary">Submit Request</button>
        </form>
      </Modal>
    </div>
  );
}
