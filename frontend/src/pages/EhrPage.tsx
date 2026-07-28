import React, { useState } from 'react';
import { api } from '../api';
import { EhrRecord, PageResponse } from '../types';
import { formatDate } from '../utils';
import { EmptyState } from '../components/EmptyState';

interface Props {
  toast: {
    success: (msg: string) => void;
    error: (msg: string) => void;
  };
}

export default function EhrPage({ toast }: Props) {
  const [form, setForm] = useState({
    patientId: '', doctorId: '', diagnosis: '', vitalBp: '120/80', vitalHeartRate: '72', vitalTemp: '98.6', vitalWeight: '70', doctorNotes: ''
  });
  const [submitting, setSubmitting] = useState(false);

  const [searchPatientId, setSearchPatientId] = useState('');
  const [records, setRecords] = useState<EhrRecord[]>([]);
  const [recordsLoading, setRecordsLoading] = useState(false);

  const handleConsultSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const payload = {
        ...form,
        vitalHeartRate: Number(form.vitalHeartRate),
        vitalTemp: Number(form.vitalTemp),
        vitalWeight: Number(form.vitalWeight)
      };
      await api.post<EhrRecord>('/hospital/ehr/record', payload);
      toast.success('Consultation recorded');
      setForm({ patientId: '', doctorId: '', diagnosis: '', vitalBp: '120/80', vitalHeartRate: '72', vitalTemp: '98.6', vitalWeight: '70', doctorNotes: '' });
    } catch (err: any) {
      toast.error(err.message || 'Error saving consultation');
    } finally {
      setSubmitting(false);
    }
  };

  const fetchRecords = async (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    if (!searchPatientId) return;
    setRecordsLoading(true);
    try {
      const response = await api.get<PageResponse<EhrRecord>>(`/hospital/ehr/patient/${searchPatientId}/records?page=0&size=20`);
      setRecords(response.data.content || []);
    } catch (err: any) {
      toast.error(err.message || 'Error fetching records');
    } finally {
      setRecordsLoading(false);
    }
  };

  return (
    <div className="page-container animate-fade-in">
      <header className="page-header flex flex-between mb-6">
        <div>
          <h1 className="page-title">Electronic Health Records</h1>
          <p className="page-subtitle">Patient consultations and clinical medical history</p>
        </div>
      </header>

      <div className="card mb-8">
        <h2 className="section-title mb-4">New Consultation</h2>
        <form onSubmit={handleConsultSubmit} className="flex flex-col gap-4">
          <div className="form-row-2 flex gap-4">
            <div className="flex-1">
              <label className="label">Patient ID (UUID) *</label>
              <input required type="text" className="input w-full" value={form.patientId} onChange={e => setForm({...form, patientId: e.target.value})} placeholder="Patient UUID" />
            </div>
            <div className="flex-1">
              <label className="label">Doctor ID (UUID) *</label>
              <input required type="text" className="input w-full" value={form.doctorId} onChange={e => setForm({...form, doctorId: e.target.value})} placeholder="Doctor UUID" />
            </div>
          </div>
          <div className="form-group">
            <label className="label">Diagnosis *</label>
            <input required type="text" className="input w-full" value={form.diagnosis} onChange={e => setForm({...form, diagnosis: e.target.value})} placeholder="e.g. Acute Bronchitis & Hypertension" />
          </div>
          <div className="form-row-4 flex gap-4">
            <div className="flex-1">
              <label className="label">Blood Pressure</label>
              <input type="text" className="input w-full" placeholder="120/80" value={form.vitalBp} onChange={e => setForm({...form, vitalBp: e.target.value})} />
            </div>
            <div className="flex-1">
              <label className="label">Heart Rate (bpm)</label>
              <input type="number" className="input w-full" value={form.vitalHeartRate} onChange={e => setForm({...form, vitalHeartRate: e.target.value})} />
            </div>
            <div className="flex-1">
              <label className="label">Temp (°F)</label>
              <input type="number" step="0.1" className="input w-full" value={form.vitalTemp} onChange={e => setForm({...form, vitalTemp: e.target.value})} />
            </div>
            <div className="flex-1">
              <label className="label">Weight (kg)</label>
              <input type="number" step="0.1" className="input w-full" value={form.vitalWeight} onChange={e => setForm({...form, vitalWeight: e.target.value})} />
            </div>
          </div>
          <div className="form-group">
            <label className="label">Doctor Notes</label>
            <textarea className="textarea w-full" rows={4} value={form.doctorNotes} onChange={e => setForm({...form, doctorNotes: e.target.value})} placeholder="Clinical notes, dosage, advice..." />
          </div>
          <div className="flex justify-end">
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Saving...' : 'Save Clinical EHR Record'}
            </button>
          </div>
        </form>
      </div>

      <div className="card">
        <h2 className="section-title mb-4">Patient Records Timeline</h2>
        <form onSubmit={fetchRecords} className="flex gap-2 mb-6">
          <input type="text" className="input flex-1" placeholder="Enter Patient ID (UUID)..." value={searchPatientId} onChange={e => setSearchPatientId(e.target.value)} />
          <button type="submit" className="btn btn-secondary">Load Records</button>
        </form>

        {recordsLoading ? (
          <div>Loading records...</div>
        ) : records.length === 0 ? (
          <EmptyState title="No records found for this patient" />
        ) : (
          <div className="flex flex-col gap-4">
            {records.map(record => (
              <div key={record.id} className="card p-4">
                <div className="flex flex-between text-sm mb-2" style={{ color: 'var(--text-muted)' }}>
                  <span>{formatDate(record.visitDate)}</span>
                  <span>Doctor: {record.doctorId?.substring(0,8)}</span>
                </div>
                <h3 className="font-semibold text-lg mb-2">Diagnosis: {record.diagnosis}</h3>
                <div className="flex gap-4 text-sm mb-4" style={{ color: 'var(--text-secondary)' }}>
                  <div>BP: {record.vitalBp || '-'}</div>
                  <div>HR: {record.vitalHeartRate || '-'} bpm</div>
                  <div>Temp: {record.vitalTemp || '-'} °F</div>
                  <div>Weight: {record.vitalWeight || '-'} kg</div>
                </div>
                <p className="text-sm p-3 rounded" style={{ backgroundColor: 'var(--bg-tertiary)' }}>
                  {record.doctorNotes || 'No notes'}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
