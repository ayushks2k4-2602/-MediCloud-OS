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

export default function AuditPage({ toast }: ToastProps) {
  const [logs, setLogs] = useState([]);
  const [loadingLogs, setLoadingLogs] = useState(false);

  // AI Copilot
  const [patientId, setPatientId] = useState('');
  const [aiNotes, setAiNotes] = useState([]);
  const [loadingAi, setLoadingAi] = useState(false);

  const [isAiModalOpen, setIsAiModalOpen] = useState(false);
  const [aiForm, setAiForm] = useState({ patientId: '', summaryType: 'VISIT_NOTES', content: '' });

  const fetchLogs = useCallback(async () => {
    setLoadingLogs(true);
    try {
      const res = await api.get<any>('/hospital/audit?page=0&size=20');
      setLogs(res.data.content || res.data);
    } catch (err) {
      toast.error('Failed to fetch audit logs');
    } finally {
      setLoadingLogs(false);
    }
  }, [toast]);

  useEffect(() => {
    fetchLogs();
  }, [fetchLogs]);

  const handleLookupAiNotes = async () => {
    if (!patientId) return;
    setLoadingAi(true);
    try {
      const res = await api.get<any>(`/hospital/ai-copilot/patient/${patientId}?page=0&size=20`);
      setAiNotes(res.data.content || res.data);
    } catch (err) {
      toast.error('Failed to fetch AI notes');
    } finally {
      setLoadingAi(false);
    }
  };

  const handleGenerateSummary = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/hospital/ai-copilot', aiForm);
      toast.success('AI summary generated');
      setIsAiModalOpen(false);
      if (aiForm.patientId === patientId) {
        handleLookupAiNotes();
      }
    } catch (err) {
      toast.error('Failed to generate summary');
    }
  };

  return (
    <div className="animate-fade-in flex flex-col gap-6">
      <header className="page-header">
        <h1 className="page-title">Audit & AI Copilot</h1>
        <p className="page-subtitle">System audit logs and AI-assisted clinical tools.</p>
      </header>

      <div className="card">
        <h2 className="section-title mb-4">Audit Logs</h2>
        {loadingLogs ? <div>Loading...</div> : logs.length === 0 ? <EmptyState title="No logs found" /> : (
          <DataTable
            columns={[
              { key: 'action', label: 'Action', render: (row: any) => <Badge variant="neutral" label={row.action} /> },
              { key: 'entityType', label: 'Entity Type' },
              { key: 'entityId', label: 'Entity ID' },
              { key: 'userEmail', label: 'User Email' },
              { key: 'ipAddress', label: 'IP Address' },
              { key: 'details', label: 'Details' },
              { key: 'createdAt', label: 'Timestamp' }
            ]}
            data={logs}
          />
        )}
      </div>

      <div className="card">
        <div className="flex flex-between mb-4">
          <h2 className="section-title">AI Clinical Copilot</h2>
          <button className="btn btn-primary" onClick={() => setIsAiModalOpen(true)}>Generate AI Summary</button>
        </div>

        <div className="p-4 bg-slate-800 rounded-lg mb-4 text-warning border border-yellow-700/50">
          ⚠️ <strong>Advisory:</strong> AI-generated content requires human clinical review before making medical decisions.
        </div>

        <div className="flex gap-4 mb-4">
          <input className="input" placeholder="Enter Patient ID" value={patientId} onChange={e => setPatientId(e.target.value)} />
          <button className="btn btn-secondary" onClick={handleLookupAiNotes}>Lookup Notes</button>
        </div>

        {loadingAi ? <div>Loading...</div> : aiNotes.length > 0 ? (
          <div className="flex flex-col gap-4">
            {aiNotes.map((note: any) => (
              <div key={note.id} className="stat-card">
                <div className="flex flex-between mb-2">
                  <Badge variant="info" label={note.summaryType} />
                  <span className="text-sm text-slate-400">{note.timestamp}</span>
                </div>
                <p className="text-sm mb-4">{note.contentPreview || note.content}</p>
                <div className="flex items-center gap-2">
                  <input type="checkbox" checked={note.humanReviewed} readOnly />
                  <label className="text-sm">Human Reviewed</label>
                </div>
              </div>
            ))}
          </div>
        ) : (
          patientId && !loadingAi && <EmptyState title="No AI notes found for this patient" />
        )}
      </div>

      <Modal isOpen={isAiModalOpen} onClose={() => setIsAiModalOpen(false)} title="Generate AI Summary">
        <form onSubmit={handleGenerateSummary} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Patient ID</label><input className="input" required value={aiForm.patientId} onChange={e => setAiForm({...aiForm, patientId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Summary Type</label>
            <select className="select" required value={aiForm.summaryType} onChange={e => setAiForm({...aiForm, summaryType: e.target.value})}>
              <option value="SOAP_SUMMARY">SOAP Summary</option>
              <option value="VISIT_NOTES">Visit Notes</option>
              <option value="DISCHARGE_SUMMARY">Discharge Summary</option>
            </select>
          </div>
          <div className="form-group"><label className="label">Content / Transcript</label>
            <textarea className="textarea" required value={aiForm.content} onChange={e => setAiForm({...aiForm, content: e.target.value})} rows={5} />
          </div>
          <button type="submit" className="btn btn-primary">Generate</button>
        </form>
      </Modal>

    </div>
  );
}
