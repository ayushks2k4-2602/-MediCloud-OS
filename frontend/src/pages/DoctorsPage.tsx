import React, { useState, useEffect, useCallback, ChangeEvent, FormEvent } from 'react';
import { api } from '../api';
import { Doctor, PageResponse } from '../types';
import { formatCurrency } from '../utils';
import { SPECIALIZATIONS } from '../constants';
import Modal from '../components/Modal';
import { Badge } from '../components/Badge';
import { ConfirmDialog } from '../components/ConfirmDialog';
import { EmptyState } from '../components/EmptyState';

interface DoctorsPageProps {
  toast: {
    success: (msg: string) => void;
    error: (msg: string) => void;
  };
}

export default function DoctorsPage({ toast }: DoctorsPageProps) {
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [deleteDoctorId, setDeleteDoctorId] = useState<string | null>(null);
  
  const [formData, setFormData] = useState({
    specialization: 'Cardiology',
    qualification: 'MD, FACC',
    consultationFee: '150',
    licenseNumber: `LIC-${Math.floor(100000 + Math.random() * 900000)}`
  });

  const fetchDoctors = useCallback(async () => {
    setIsLoading(true);
    try {
      const res = await api.get<PageResponse<Doctor>>('/hospital/doctors?page=0&size=50');
      setDoctors(res.data.content || []);
    } catch (err: any) {
      toast.error(err.message || 'Failed to fetch doctors');
    } finally {
      setIsLoading(false);
    }
  }, [toast]);

  useEffect(() => {
    fetchDoctors();
  }, [fetchDoctors]);

  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      const payload = {
        ...formData,
        consultationFee: Number(formData.consultationFee),
        isAvailable: true
      };
      await api.post<Doctor>('/hospital/doctors', payload);
      toast.success('Doctor added successfully');
      setIsModalOpen(false);
      setFormData({
        specialization: 'Cardiology', qualification: 'MD, FACC', consultationFee: '150',
        licenseNumber: `LIC-${Math.floor(100000 + Math.random() * 900000)}`
      });
      fetchDoctors();
    } catch (err: any) {
      toast.error(err.message || 'Failed to add doctor');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteDoctorId) return;
    try {
      await api.delete(`/hospital/doctors/${deleteDoctorId}`);
      toast.success('Doctor removed successfully');
      fetchDoctors();
    } catch (err: any) {
      toast.error(err.message || 'Failed to remove doctor');
    } finally {
      setDeleteDoctorId(null);
    }
  };

  return (
    <div className="page-container animate-fade-in">
      <div className="page-header flex flex-between mb-6">
        <div>
          <h1 className="page-title">Doctor Directory</h1>
          <p className="page-subtitle">Manage medical staff and consultants</p>
        </div>
        <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
          + Add Doctor
        </button>
      </div>

      {isLoading ? (
        <div style={{ display: 'grid', gap: '1.5rem', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))' }}>
          {[1, 2, 3].map(i => (
            <div key={i} className="card p-6 animate-pulse bg-slate-800" style={{ height: '200px' }}></div>
          ))}
        </div>
      ) : doctors.length === 0 ? (
        <EmptyState icon="⚕️" title="No doctors registered yet" />
      ) : (
        <div style={{ display: 'grid', gap: '1.5rem', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))' }}>
          {doctors.map(doctor => (
            <div key={doctor.id} className="card p-5 flex flex-col gap-3" style={{ border: '1px solid var(--border)', backgroundColor: 'var(--card-bg)' }}>
              <div className="flex flex-between items-start">
                <div>
                  <h3 className="text-lg font-semibold" style={{ color: 'var(--text-main)' }}>{doctor.qualification || 'Medical Specialist'}</h3>
                  <Badge variant="info" label={doctor.specialization} />
                </div>
                <div 
                  style={{ width: '12px', height: '12px', borderRadius: '50%', backgroundColor: doctor.isAvailable !== false ? '#22c55e' : '#6b7280' }} 
                  title={doctor.isAvailable !== false ? 'Available' : 'Unavailable'} 
                />
              </div>
              
              <div className="text-sm mt-2" style={{ color: 'var(--text-muted)' }}>
                <p><strong>License:</strong> <span className="font-mono text-teal-600" style={{ fontFamily: 'monospace', color: '#0d9488' }}>{doctor.licenseNumber}</span></p>
                <p><strong>Fee:</strong> {formatCurrency(doctor.consultationFee)}</p>
              </div>

              <div className="flex gap-2 mt-auto pt-4" style={{ borderTop: '1px solid var(--border)' }}>
                <button className="btn btn-danger btn-sm" style={{ flex: 1 }} onClick={() => setDeleteDoctorId(doctor.id)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Add Doctor">
        <form onSubmit={handleSubmit}>
          <div className="modal-body flex flex-col gap-4">
            <div className="form-group">
              <label className="label">Specialization *</label>
              <select required name="specialization" value={formData.specialization} onChange={handleInputChange} className="select w-full">
                {SPECIALIZATIONS?.map(spec => (
                  <option key={spec} value={spec}>{spec}</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label className="label">Qualification *</label>
              <input required type="text" name="qualification" value={formData.qualification} onChange={handleInputChange} className="input w-full" placeholder="e.g. MD, FACC" />
            </div>

            <div className="form-row-2 flex gap-4">
              <div className="form-group flex-1">
                <label className="label">License Number *</label>
                <input required type="text" name="licenseNumber" value={formData.licenseNumber} onChange={handleInputChange} className="input w-full font-mono" style={{ fontFamily: 'monospace' }} />
              </div>
              <div className="form-group flex-1">
                <label className="label">Consultation Fee *</label>
                <input required type="number" name="consultationFee" min="0" step="0.01" value={formData.consultationFee} onChange={handleInputChange} className="input w-full" />
              </div>
            </div>
          </div>
          
          <div className="modal-footer flex gap-4 flex-between mt-6">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
              {isSubmitting ? 'Submitting...' : 'Add Doctor'}
            </button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={!!deleteDoctorId}
        title="Remove Doctor"
        message="Are you sure you want to remove this doctor? This action cannot be undone."
        onConfirm={handleDelete}
        onClose={() => setDeleteDoctorId(null)}
      />
    </div>
  );
}
