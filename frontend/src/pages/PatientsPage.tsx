import React, { useState, useEffect, useCallback, ChangeEvent, FormEvent } from 'react';
import { api } from '../api';
import { useDebounce, usePagination } from '../hooks';
import { Patient, PageResponse } from '../types';
import { buildQueryString } from '../utils';
import { BLOOD_GROUPS, DEFAULT_PAGE_SIZE, DEBOUNCE_DELAY } from '../constants';
import { DataTable } from '../components/DataTable';
import Modal from '../components/Modal';
import { Badge } from '../components/Badge';
import { EmptyState } from '../components/EmptyState';

interface PatientsPageProps {
  toast: {
    success: (msg: string) => void;
    error: (msg: string) => void;
  };
}

export default function PatientsPage({ toast }: PatientsPageProps) {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [bloodGroup, setBloodGroup] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  
  const debouncedSearch = useDebounce(search, DEBOUNCE_DELAY || 300);
  const pagination = usePagination(DEFAULT_PAGE_SIZE || 20);

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    phone: '',
    email: '',
    gender: 'Male',
    bloodGroup: 'O+',
    dateOfBirth: '1990-01-01',
    address: '',
    emergencyContact: '',
    insuranceProvider: '',
    insurancePolicyNumber: ''
  });

  const fetchPatients = useCallback(async () => {
    setIsLoading(true);
    try {
      const query = buildQueryString({
        page: pagination.page,
        size: pagination.size,
        search: debouncedSearch,
        bloodGroup
      });
      const res = await api.get<PageResponse<Patient>>(`/hospital/patients${query}`);
      setPatients(res.data.content || []);
      pagination.updateFromResponse({
        totalPages: res.data.totalPages || 1,
        totalElements: res.data.totalElements || 0
      });
    } catch (err: any) {
      toast.error(err.message || 'Failed to fetch patients');
    } finally {
      setIsLoading(false);
    }
  }, [pagination.page, pagination.size, debouncedSearch, bloodGroup, toast]);

  useEffect(() => {
    fetchPatients();
  }, [fetchPatients]);

  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await api.post<Patient>('/hospital/patients', formData);
      toast.success('Patient registered successfully');
      setIsModalOpen(false);
      setFormData({
        firstName: '', lastName: '', phone: '', email: '', gender: 'Male', bloodGroup: 'O+',
        dateOfBirth: '1990-01-01', address: '', emergencyContact: '', insuranceProvider: '', insurancePolicyNumber: ''
      });
      fetchPatients();
    } catch (err: any) {
      toast.error(err.message || 'Failed to register patient');
    } finally {
      setIsSubmitting(false);
    }
  };

  const columns = [
    {
      key: 'patientCode',
      label: 'Patient Code',
      render: (row: Patient) => <span className="font-mono text-teal-600" style={{ color: '#0d9488', fontFamily: 'monospace' }}>{row.patientCode}</span>
    },
    {
      key: 'name',
      label: 'Full Name',
      render: (row: Patient) => `${row.firstName} ${row.lastName}`
    },
    { key: 'phone', label: 'Phone' },
    { key: 'gender', label: 'Gender' },
    {
      key: 'bloodGroup',
      label: 'Blood Group',
      render: (row: Patient) => row.bloodGroup ? <Badge variant="danger" label={row.bloodGroup} /> : '-'
    },
    { key: 'insuranceProvider', label: 'Insurance' }
  ];

  return (
    <div className="page-container animate-fade-in">
      <div className="page-header flex flex-between mb-6">
        <div>
          <h1 className="page-title">Patient Directory</h1>
          <p className="page-subtitle">Manage hospital patients and records</p>
        </div>
        <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
          + Register Patient
        </button>
      </div>

      <div className="card mb-4">
        <div className="flex gap-4 mb-4">
          <div className="form-group flex-1">
            <input
              type="text"
              className="input w-full"
              placeholder="Search patients..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
          <div className="form-group">
            <select
              className="select w-full"
              value={bloodGroup}
              onChange={(e) => setBloodGroup(e.target.value)}
            >
              <option value="">All Blood Groups</option>
              {BLOOD_GROUPS?.map(bg => (
                <option key={bg} value={bg}>{bg}</option>
              ))}
            </select>
          </div>
        </div>

        {patients.length === 0 && !isLoading ? (
          <EmptyState icon="👤" title="No patients registered yet" />
        ) : (
          <DataTable
            columns={columns}
            data={patients}
            loading={isLoading}
            page={pagination.page + 1}
            totalPages={pagination.totalPages}
            onPageChange={(p) => pagination.goToPage(p - 1)}
          />
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Register Patient">
        <form onSubmit={handleSubmit}>
          <div className="modal-body flex flex-col gap-4">
            <div className="form-row-2 flex gap-4">
              <div className="form-group flex-1">
                <label className="label">First Name *</label>
                <input required type="text" name="firstName" value={formData.firstName} onChange={handleInputChange} className="input w-full" />
              </div>
              <div className="form-group flex-1">
                <label className="label">Last Name *</label>
                <input required type="text" name="lastName" value={formData.lastName} onChange={handleInputChange} className="input w-full" />
              </div>
            </div>

            <div className="form-row-2 flex gap-4">
              <div className="form-group flex-1">
                <label className="label">Phone *</label>
                <input required type="text" name="phone" value={formData.phone} onChange={handleInputChange} className="input w-full" />
              </div>
              <div className="form-group flex-1">
                <label className="label">Email</label>
                <input type="email" name="email" value={formData.email} onChange={handleInputChange} className="input w-full" />
              </div>
            </div>

            <div className="form-row-2 flex gap-4">
              <div className="form-group flex-1">
                <label className="label">Gender</label>
                <select name="gender" value={formData.gender} onChange={handleInputChange} className="select w-full">
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                </select>
              </div>
              <div className="form-group flex-1">
                <label className="label">Blood Group</label>
                <select name="bloodGroup" value={formData.bloodGroup} onChange={handleInputChange} className="select w-full">
                  {BLOOD_GROUPS?.map(bg => (
                    <option key={bg} value={bg}>{bg}</option>
                  ))}
                </select>
              </div>
            </div>

            <div className="form-group">
              <label className="label">Insurance Provider</label>
              <input type="text" name="insuranceProvider" value={formData.insuranceProvider} onChange={handleInputChange} className="input w-full" placeholder="e.g. Blue Cross" />
            </div>
          </div>
          
          <div className="modal-footer flex gap-4 flex-between mt-6">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
              {isSubmitting ? 'Registering...' : 'Save Patient'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
