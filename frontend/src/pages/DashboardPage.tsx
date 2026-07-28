import React, { useState, useEffect } from 'react';
import { api } from '../api';
import type { Patient, Doctor, Bed, Invoice, PageResponse, ApiResponse } from '../types';

interface DashboardStats {
  patients: number | null;
  doctors: number | null;
  bedsAvailable: number | null;
  bedsTotal: number | null;
  revenue: number | null;
}

interface ErrorsState {
  patients: boolean;
  doctors: boolean;
  beds: boolean;
  revenue: boolean;
  recentActivity: boolean;
}

export default function DashboardPage() {
  const [stats, setStats] = useState<DashboardStats>({
    patients: null,
    doctors: null,
    bedsAvailable: null,
    bedsTotal: null,
    revenue: null,
  });
  const [recentPatients, setRecentPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState({
    stats: true,
    recentActivity: true,
  });
  const [errors, setErrors] = useState<ErrorsState>({
    patients: false,
    doctors: false,
    beds: false,
    revenue: false,
    recentActivity: false,
  });

  useEffect(() => {
    const fetchDashboardData = async () => {
      // Fetch Patients
      try {
        const res = await api.get<PageResponse<Patient>>('/hospital/patients?page=0&size=1');
        if (res.data) {
          setStats(s => ({ ...s, patients: res.data!.totalElements }));
        }
      } catch (e) {
        setErrors(err => ({ ...err, patients: true }));
      }

      // Fetch Doctors
      try {
        const res = await api.get<PageResponse<Doctor>>('/hospital/doctors?page=0&size=1');
        if (res.data) {
          setStats(s => ({ ...s, doctors: res.data!.totalElements }));
        }
      } catch (e) {
        setErrors(err => ({ ...err, doctors: true }));
      }

      // Fetch Beds
      try {
        const res = await api.get<PageResponse<Bed>>('/hospital/beds?page=0&size=100');
        if (res.data && res.data.content) {
          const content = res.data.content;
          const available = content.filter(b => b.status === 'AVAILABLE').length;
          setStats(s => ({ ...s, bedsAvailable: available, bedsTotal: content.length }));
        }
      } catch (e) {
        setErrors(err => ({ ...err, beds: true }));
      }

      // Fetch Revenue
      try {
        const res = await api.get<PageResponse<Invoice>>('/hospital/invoices?page=0&size=100');
        if (res.data && res.data.content) {
          const content = res.data.content;
          const revenue = content
            .filter(i => i.status === 'PAID')
            .reduce((sum, i) => sum + (i.netAmount || 0), 0);
          setStats(s => ({ ...s, revenue }));
        }
      } catch (e) {
        setErrors(err => ({ ...err, revenue: true }));
      }

      setLoading(l => ({ ...l, stats: false }));

      // Fetch Recent Activity
      try {
        const res = await api.get<PageResponse<Patient>>('/hospital/patients?page=0&size=5');
        if (res.data && res.data.content) {
          setRecentPatients(res.data.content);
        }
      } catch (e) {
        setErrors(err => ({ ...err, recentActivity: true }));
      } finally {
        setLoading(l => ({ ...l, recentActivity: false }));
      }
    };

    fetchDashboardData();
  }, []);

  const formatCurrency = (amount: number | null) => {
    if (amount === null) return '—';
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(amount);
  };

  return (
    <div className="flex flex-col gap-6 animate-fade-in w-full">
      <div>
        <h1 className="page-title">Hospital Operations Dashboard</h1>
        <p className="page-subtitle">Real-time statistics for Ayush Health Network</p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px' }}>
        {/* Total Patients */}
        <div className="card stat-card flex flex-col gap-2">
          <div className="stat-label">Total Patients</div>
          <div className="stat-value" title={errors.patients ? "Failed to load data" : ""}>
            {loading.stats && !errors.patients ? <div className="spinner" style={{ width: '24px', height: '24px' }}></div> : (errors.patients ? '—' : stats.patients?.toLocaleString() || '0')}
          </div>
        </div>

        {/* Active Doctors */}
        <div className="card stat-card flex flex-col gap-2">
          <div className="stat-label">Active Doctors</div>
          <div className="stat-value" title={errors.doctors ? "Failed to load data" : ""}>
            {loading.stats && !errors.doctors ? <div className="spinner" style={{ width: '24px', height: '24px' }}></div> : (errors.doctors ? '—' : stats.doctors?.toLocaleString() || '0')}
          </div>
        </div>

        {/* Available Beds */}
        <div className="card stat-card flex flex-col gap-2">
          <div className="stat-label">Available Beds</div>
          <div className="stat-value" title={errors.beds ? "Failed to load data" : ""}>
            {loading.stats && !errors.beds ? <div className="spinner" style={{ width: '24px', height: '24px' }}></div> : (errors.beds ? '—' : `${stats.bedsAvailable || 0} / ${stats.bedsTotal || 0}`)}
          </div>
        </div>

        {/* Revenue */}
        <div className="card stat-card flex flex-col gap-2">
          <div className="stat-label">Revenue (Paid)</div>
          <div className="stat-value" title={errors.revenue ? "Failed to load data" : ""}>
            {loading.stats && !errors.revenue ? <div className="spinner" style={{ width: '24px', height: '24px' }}></div> : (errors.revenue ? '—' : formatCurrency(stats.revenue))}
          </div>
        </div>
      </div>

      <div className="flex flex-col gap-4">
        <h2 className="section-title">Quick Actions</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '16px' }}>
          <button className="card btn flex flex-center" style={{ padding: '16px', background: 'var(--bg-card, #111827)' }}>Register Patient</button>
          <button className="card btn flex flex-center" style={{ padding: '16px', background: 'var(--bg-card, #111827)' }}>Schedule Appointment</button>
          <button className="card btn flex flex-center" style={{ padding: '16px', background: 'var(--bg-card, #111827)' }}>Create Invoice</button>
          <button className="card btn flex flex-center" style={{ padding: '16px', background: 'var(--bg-card, #111827)' }}>View Lab Orders</button>
        </div>
      </div>

      <div className="flex flex-col gap-4">
        <h2 className="section-title">Recent Activity</h2>
        <div className="card flex flex-col">
          {loading.recentActivity ? (
            <div className="flex flex-center" style={{ padding: '32px' }}>
              <div className="spinner" style={{ width: '24px', height: '24px' }}></div>
            </div>
          ) : errors.recentActivity ? (
            <div className="empty-state">Failed to load recent activity.</div>
          ) : recentPatients.length === 0 ? (
            <div className="empty-state">No recent patients registered.</div>
          ) : (
            <div className="flex flex-col gap-4">
              {recentPatients.map(patient => (
                <div key={patient.id} className="flex flex-between" style={{ paddingBottom: '12px', borderBottom: '1px solid var(--border, #1e293b)' }}>
                  <div className="flex flex-col">
                    <span style={{ fontWeight: 600 }}>{patient.firstName} {patient.lastName}</span>
                    <span style={{ fontSize: '12px', color: 'var(--text-muted, #94a3b8)' }}>Code: {patient.patientCode}</span>
                  </div>
                  <div>
                    <span className="badge badge-success">Registered</span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
