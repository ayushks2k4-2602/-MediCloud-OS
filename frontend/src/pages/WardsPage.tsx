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

export default function WardsPage({ toast }: ToastProps) {
  const [wards, setWards] = useState([]);
  const [beds, setBeds] = useState([]);
  const [loadingWards, setLoadingWards] = useState(false);
  const [loadingBeds, setLoadingBeds] = useState(false);

  const [isWardModalOpen, setIsWardModalOpen] = useState(false);
  const [isBedModalOpen, setIsBedModalOpen] = useState(false);

  const [wardForm, setWardForm] = useState({ name: '', type: 'GENERAL', capacity: 10 });
  const [bedForm, setBedForm] = useState({ bedCode: '', wardId: '', patientId: '', status: 'AVAILABLE', dailyRate: 0 });

  const fetchWards = useCallback(async () => {
    setLoadingWards(true);
    try {
      const res = await api.get<any>('/hospital/wards');
      setWards(res.data);
    } catch (err) {
      toast.error('Failed to fetch wards');
    } finally {
      setLoadingWards(false);
    }
  }, [toast]);

  const fetchBeds = useCallback(async () => {
    setLoadingBeds(true);
    try {
      const res = await api.get<any>('/hospital/beds?page=0&size=50');
      setBeds(res.data.content || res.data);
    } catch (err) {
      toast.error('Failed to fetch beds');
    } finally {
      setLoadingBeds(false);
    }
  }, [toast]);

  useEffect(() => {
    fetchWards();
    fetchBeds();
  }, [fetchWards, fetchBeds]);

  const handleCreateWard = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/hospital/wards', wardForm);
      toast.success('Ward created successfully');
      setIsWardModalOpen(false);
      fetchWards();
    } catch (err) {
      toast.error('Failed to create ward');
    }
  };

  const handleAllocateBed = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/hospital/beds', bedForm);
      toast.success('Bed allocated successfully');
      setIsBedModalOpen(false);
      fetchBeds();
    } catch (err) {
      toast.error('Failed to allocate bed');
    }
  };

  const getBedStatusBadge = (status: string) => {
    switch (status) {
      case 'AVAILABLE': return <Badge variant="success" label="AVAILABLE" />;
      case 'OCCUPIED': return <Badge variant="danger" label="OCCUPIED" />;
      case 'MAINTENANCE': return <Badge variant="warning" label="MAINTENANCE" />;
      case 'RESERVED': return <Badge variant="info" label="RESERVED" />;
      default: return <Badge variant="neutral" label={status} />;
    }
  };

  return (
    <>
      <div className="animate-fade-in flex flex-col gap-6">
        <header className="page-header">
          <h1 className="page-title">Wards & Beds</h1>
          <p className="page-subtitle">Manage hospital wards, bed allocations, and occupancy.</p>
        </header>

        <div className="card">
          <div className="flex flex-between mb-4">
            <h2 className="section-title">Wards</h2>
            <button className="btn btn-primary" onClick={() => setIsWardModalOpen(true)}>+ Create Ward</button>
          </div>
          {loadingWards ? <div>Loading...</div> : wards.length === 0 ? <EmptyState title="No wards found" /> : (
            <div className="grid grid-cols-3 gap-4">
              {wards.map((w: any) => (
                <div key={w.id} className="stat-card">
                  <div className="flex flex-between mb-2">
                    <h3 className="stat-label text-lg font-bold">{w.name}</h3>
                    <Badge variant="info" label={w.type} />
                  </div>
                  <div className="text-sm mb-1">Capacity: {w.capacity}</div>
                  <div className="text-sm mb-2">Occupied: {w.currentOccupancy}</div>
                  <div className="w-full bg-slate-700 h-2 rounded overflow-hidden">
                    <div className="bg-teal-500 h-full" style={{ width: `${(w.currentOccupancy / w.capacity) * 100}%` }}></div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="card">
          <div className="flex flex-between mb-4">
            <h2 className="section-title">Beds</h2>
            <button className="btn btn-primary" onClick={() => setIsBedModalOpen(true)}>+ Allocate Bed</button>
          </div>
          {loadingBeds ? <div>Loading...</div> : beds.length === 0 ? <EmptyState title="No beds found" /> : (
            <DataTable
              columns={[
                { key: 'bedCode', label: 'Bed Code' },
                { key: 'wardId', label: 'Ward ID' },
                { key: 'patientId', label: 'Patient ID' },
                { key: 'status', label: 'Status', render: (row: any) => getBedStatusBadge(row.status) },
                { key: 'dailyRate', label: 'Daily Rate' }
              ]}
              data={beds}
            />
          )}
        </div>

        <Modal isOpen={isWardModalOpen} onClose={() => setIsWardModalOpen(false)} title="Create Ward">
          <form onSubmit={handleCreateWard} className="flex flex-col gap-4">
            <div className="form-group"><label className="label">Name</label><input className="input" required value={wardForm.name} onChange={e => setWardForm({...wardForm, name: e.target.value})} /></div>
            <div className="form-group"><label className="label">Type</label>
              <select className="select" required value={wardForm.type} onChange={e => setWardForm({...wardForm, type: e.target.value})}>
                <option value="GENERAL">General</option>
                <option value="ICU">ICU</option>
                <option value="PEDIATRIC">Pediatric</option>
                <option value="MATERNITY">Maternity</option>
              </select>
            </div>
            <div className="form-group"><label className="label">Capacity</label><input type="number" className="input" required value={wardForm.capacity} onChange={e => setWardForm({...wardForm, capacity: +e.target.value})} /></div>
            <button type="submit" className="btn btn-primary">Create</button>
          </form>
        </Modal>

        <Modal isOpen={isBedModalOpen} onClose={() => setIsBedModalOpen(false)} title="Allocate Bed">
          <form onSubmit={handleAllocateBed} className="flex flex-col gap-4">
            <div className="form-group"><label className="label">Bed Code</label><input className="input" required value={bedForm.bedCode} onChange={e => setBedForm({...bedForm, bedCode: e.target.value})} /></div>
            <div className="form-group"><label className="label">Ward ID</label><input className="input" required value={bedForm.wardId} onChange={e => setBedForm({...bedForm, wardId: e.target.value})} /></div>
            <div className="form-group"><label className="label">Patient ID</label><input className="input" value={bedForm.patientId} onChange={e => setBedForm({...bedForm, patientId: e.target.value})} /></div>
            <div className="form-group"><label className="label">Status</label>
              <select className="select" required value={bedForm.status} onChange={e => setBedForm({...bedForm, status: e.target.value})}>
                <option value="AVAILABLE">Available</option>
                <option value="OCCUPIED">Occupied</option>
                <option value="MAINTENANCE">Maintenance</option>
                <option value="RESERVED">Reserved</option>
              </select>
            </div>
            <div className="form-group"><label className="label">Daily Rate</label><input type="number" className="input" required value={bedForm.dailyRate} onChange={e => setBedForm({...bedForm, dailyRate: +e.target.value})} /></div>
            <button type="submit" className="btn btn-primary">Allocate</button>
          </form>
        </Modal>
      </div>
    </>
  );
}
