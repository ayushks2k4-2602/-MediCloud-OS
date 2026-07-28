import React, { useState, useEffect, useCallback } from 'react';
import { api } from '../api';
import { usePagination } from '../hooks';
import { Appointment, PageResponse } from '../types';
import { formatDate } from '../utils';
import { APPOINTMENT_STATUSES } from '../constants';
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

const statusList = Object.keys(APPOINTMENT_STATUSES);

export default function AppointmentsPage({ toast }: Props) {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [statusFilter, setStatusFilter] = useState('All');
  
  const pagination = usePagination(20);

  const [isBookModalOpen, setIsBookModalOpen] = useState(false);
  const [bookForm, setBookForm] = useState({
    patientId: '', doctorId: '', appointmentDate: '', startTime: '09:00', endTime: '09:30', notes: ''
  });

  const fetchAppointments = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const endpoint = `/hospital/appointments?page=${pagination.page}&size=${pagination.size}`;
      const response = await api.get<PageResponse<Appointment>>(endpoint);
      setAppointments(response.data.content || []);
      pagination.updateFromResponse({
        totalPages: response.data.totalPages || 1,
        totalElements: response.data.totalElements || 0
      });
    } catch (err: any) {
      setError(err.message || 'An error occurred');
      toast.error('Failed to load appointments');
    } finally {
      setLoading(false);
    }
  }, [pagination.page, pagination.size, toast]);

  useEffect(() => {
    fetchAppointments();
  }, [fetchAppointments]);

  const handleBookSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post<Appointment>('/hospital/appointments', bookForm);
      toast.success('Appointment booked successfully');
      setIsBookModalOpen(false);
      fetchAppointments();
    } catch (err: any) {
      toast.error('Error booking appointment');
    }
  };

  const handleStatusUpdate = async (id: string, newStatus: string) => {
    try {
      await api.put<Appointment>(`/hospital/appointments/${id}/status?status=${newStatus}`, {});
      toast.success('Status updated');
      fetchAppointments();
    } catch (err: any) {
      toast.error('Error updating status');
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'SCHEDULED': return <Badge variant="info" label="SCHEDULED" />;
      case 'CONFIRMED': return <Badge variant="success" label="CONFIRMED" />;
      case 'COMPLETED': return <Badge variant="success" label="COMPLETED" />;
      case 'CANCELLED': return <Badge variant="danger" label="CANCELLED" />;
      case 'NO_SHOW': return <Badge variant="warning" label="NO_SHOW" />;
      default: return <Badge variant="neutral" label={status || 'SCHEDULED'} />;
    }
  };

  const columns = [
    { key: 'appointmentDate', label: 'Date', render: (row: Appointment) => formatDate(row.appointmentDate) },
    { key: 'time', label: 'Time', render: (row: Appointment) => `${row.startTime || ''} - ${row.endTime || ''}` },
    { key: 'patientId', label: 'Patient ID', render: (row: Appointment) => row.patientId ? (row.patientId.length > 10 ? row.patientId.substring(0, 8) + '...' : row.patientId) : '-' },
    { key: 'doctorId', label: 'Doctor ID', render: (row: Appointment) => row.doctorId ? (row.doctorId.length > 10 ? row.doctorId.substring(0, 8) + '...' : row.doctorId) : '-' },
    { key: 'status', label: 'Status', render: (row: Appointment) => getStatusBadge(row.status) },
    { key: 'notes', label: 'Notes' },
    {
      key: 'actions',
      label: 'Actions',
      render: (row: Appointment) => (
        <select 
          className="select text-xs" 
          value={row.status} 
          onChange={(e) => handleStatusUpdate(row.id, e.target.value)}
        >
          {statusList.map(s => <option key={s} value={s}>{s}</option>)}
        </select>
      )
    }
  ];

  const filteredAppointments = statusFilter === 'All' 
    ? appointments 
    : appointments.filter(a => a.status === statusFilter);

  return (
    <div className="page-container animate-fade-in">
      <header className="page-header flex flex-between mb-6">
        <div>
          <h1 className="page-title">Appointments & Scheduling</h1>
          <p className="page-subtitle">Schedule and manage doctor consultations</p>
        </div>
        <button className="btn btn-primary" onClick={() => setIsBookModalOpen(true)}>+ Book Appointment</button>
      </header>

      <div className="card mb-4">
        <div className="filter-bar mb-4 flex items-center gap-2">
          <label className="label">Filter Status:</label>
          <select 
            className="select w-48"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="All">All Statuses</option>
            {statusList.map(s => <option key={s} value={s}>{s}</option>)}
          </select>
        </div>

        {loading ? (
          <div>Loading appointments...</div>
        ) : error ? (
          <div className="badge badge-danger">{error}</div>
        ) : filteredAppointments.length === 0 ? (
          <EmptyState title="No appointments scheduled" />
        ) : (
          <DataTable 
            columns={columns} 
            data={filteredAppointments}
            loading={loading}
            page={pagination.page + 1}
            totalPages={pagination.totalPages}
            onPageChange={(p) => pagination.goToPage(p - 1)}
          />
        )}
      </div>

      <Modal isOpen={isBookModalOpen} title="Book Appointment" onClose={() => setIsBookModalOpen(false)}>
        <form onSubmit={handleBookSubmit} className="flex flex-col gap-4">
          <div className="form-group">
            <label className="label">Patient ID *</label>
            <input required type="text" className="input w-full" value={bookForm.patientId} onChange={e => setBookForm({...bookForm, patientId: e.target.value})} placeholder="Patient UUID" />
          </div>
          <div className="form-group">
            <label className="label">Doctor ID *</label>
            <input required type="text" className="input w-full" value={bookForm.doctorId} onChange={e => setBookForm({...bookForm, doctorId: e.target.value})} placeholder="Doctor UUID" />
          </div>
          <div className="form-group">
            <label className="label">Date *</label>
            <input required type="date" className="input w-full" value={bookForm.appointmentDate} onChange={e => setBookForm({...bookForm, appointmentDate: e.target.value})} />
          </div>
          <div className="form-row-2 flex gap-4">
            <div className="form-group flex-1">
              <label className="label">Start Time *</label>
              <input required type="time" className="input w-full" value={bookForm.startTime} onChange={e => setBookForm({...bookForm, startTime: e.target.value})} />
            </div>
            <div className="form-group flex-1">
              <label className="label">End Time *</label>
              <input required type="time" className="input w-full" value={bookForm.endTime} onChange={e => setBookForm({...bookForm, endTime: e.target.value})} />
            </div>
          </div>
          <div className="form-group">
            <label className="label">Notes</label>
            <textarea className="textarea w-full" value={bookForm.notes} onChange={e => setBookForm({...bookForm, notes: e.target.value})} />
          </div>
          <div className="modal-footer flex flex-between mt-4">
            <button type="button" className="btn btn-secondary" onClick={() => setIsBookModalOpen(false)}>Cancel</button>
            <button type="submit" className="btn btn-primary">Book</button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
