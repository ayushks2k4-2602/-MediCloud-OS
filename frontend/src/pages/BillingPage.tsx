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

export default function BillingPage({ toast }: ToastProps) {
  const [activeTab, setActiveTab] = useState<'invoices' | 'payments' | 'providers' | 'claims'>('invoices');
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<any[]>([]);

  // Modals
  const [isInvoiceModalOpen, setIsInvoiceModalOpen] = useState(false);
  const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false);
  const [isProviderModalOpen, setIsProviderModalOpen] = useState(false);
  const [isClaimModalOpen, setIsClaimModalOpen] = useState(false);

  // Forms
  const [invoiceForm, setInvoiceForm] = useState({ patientId: '', tax: 0, discount: 0, items: [{ description: '', quantity: 1, unitPrice: 0 }] });
  const [paymentForm, setPaymentForm] = useState({ invoiceId: '', amount: 0, paymentMethod: 'CASH' });
  const [providerForm, setProviderForm] = useState({ name: '', contactEmail: '', contactPhone: '' });
  const [claimForm, setClaimForm] = useState({ patientId: '', invoiceId: '', claimAmount: 0 });

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      if (activeTab === 'invoices') {
        const res = await api.get<any>('/hospital/invoices?page=0&size=20');
        setData(res.data.content || res.data);
      } else if (activeTab === 'payments') {
        const res = await api.get<any>('/hospital/payments?page=0&size=20');
        setData(res.data.content || res.data);
      } else if (activeTab === 'providers') {
        const res = await api.get<any>('/hospital/insurance/providers');
        setData(res.data);
      } else if (activeTab === 'claims') {
        const res = await api.get<any>('/hospital/insurance/claims?page=0&size=20');
        setData(res.data.content || res.data);
      }
    } catch (err) {
      toast.error('Failed to fetch ' + activeTab);
    } finally {
      setLoading(false);
    }
  }, [activeTab, toast]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleAction = async (endpoint: string, payload: any, modalSetter: (b: boolean) => void, successMsg: string) => {
    try {
      await api.post(endpoint, payload);
      toast.success(successMsg);
      modalSetter(false);
      fetchData();
    } catch (err) {
      toast.error('Operation failed');
    }
  };

  return (
    <div className="animate-fade-in">
      <header className="page-header flex flex-between">
        <div>
          <h1 className="page-title">Billing & Insurance</h1>
          <p className="page-subtitle">Manage invoices, payments, and insurance claims.</p>
        </div>
        <div className="flex gap-4">
          <button className={`btn ${activeTab === 'invoices' ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActiveTab('invoices')}>Invoices</button>
          <button className={`btn ${activeTab === 'payments' ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActiveTab('payments')}>Payments</button>
          <button className={`btn ${activeTab === 'providers' ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActiveTab('providers')}>Providers</button>
          <button className={`btn ${activeTab === 'claims' ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActiveTab('claims')}>Claims</button>
        </div>
      </header>

      <div className="card mt-6">
        {activeTab === 'invoices' && (
          <>
            <div className="flex flex-between mb-4">
              <h2 className="section-title">Invoices</h2>
              <button className="btn btn-primary" onClick={() => setIsInvoiceModalOpen(true)}>+ Create Invoice</button>
            </div>
            {loading ? <div>Loading...</div> : data.length === 0 ? <EmptyState title="No invoices" /> : (
              <DataTable columns={[
                { key: 'invoiceNumber', label: 'Invoice #' },
                { key: 'patientId', label: 'Patient ID' },
                { key: 'totalAmount', label: 'Total' },
                { key: 'tax', label: 'Tax' },
                { key: 'discount', label: 'Discount' },
                { key: 'netAmount', label: 'Net Amount' },
                { key: 'status', label: 'Status', render: (row: any) => <Badge variant={row.status === 'PAID' ? 'success' : row.status === 'DRAFT' ? 'neutral' : 'warning'} label={row.status} /> }
              ]} data={data} />
            )}
          </>
        )}

        {activeTab === 'payments' && (
          <>
            <div className="flex flex-between mb-4">
              <h2 className="section-title">Payments</h2>
              <button className="btn btn-primary" onClick={() => setIsPaymentModalOpen(true)}>+ Process Payment</button>
            </div>
            {loading ? <div>Loading...</div> : data.length === 0 ? <EmptyState title="No payments" /> : (
              <DataTable columns={[
                { key: 'invoiceId', label: 'Invoice ID' },
                { key: 'amount', label: 'Amount' },
                { key: 'paymentMethod', label: 'Method', render: (row: any) => <Badge variant="info" label={row.paymentMethod} /> },
                { key: 'transactionReference', label: 'Ref' },
                { key: 'status', label: 'Status', render: (row: any) => <Badge variant={row.status === 'COMPLETED' ? 'success' : 'danger'} label={row.status} /> }
              ]} data={data} />
            )}
          </>
        )}

        {activeTab === 'providers' && (
          <>
            <div className="flex flex-between mb-4">
              <h2 className="section-title">Insurance Providers</h2>
              <button className="btn btn-primary" onClick={() => setIsProviderModalOpen(true)}>+ Add Provider</button>
            </div>
            {loading ? <div>Loading...</div> : data.length === 0 ? <EmptyState title="No providers" /> : (
              <div className="grid grid-cols-3 gap-4">
                {data.map(p => (
                  <div key={p.id} className="stat-card">
                    <h3 className="stat-label text-lg">{p.name}</h3>
                    <div className="text-sm mt-2">Email: {p.contactEmail}</div>
                    <div className="text-sm">Phone: {p.contactPhone}</div>
                  </div>
                ))}
              </div>
            )}
          </>
        )}

        {activeTab === 'claims' && (
          <>
            <div className="flex flex-between mb-4">
              <h2 className="section-title">Claims</h2>
              <button className="btn btn-primary" onClick={() => setIsClaimModalOpen(true)}>+ Submit Claim</button>
            </div>
            {loading ? <div>Loading...</div> : data.length === 0 ? <EmptyState title="No claims" /> : (
              <DataTable columns={[
                { key: 'claimNumber', label: 'Claim #' },
                { key: 'patientId', label: 'Patient ID' },
                { key: 'invoiceId', label: 'Invoice ID' },
                { key: 'claimAmount', label: 'Claim Amt' },
                { key: 'approvedAmount', label: 'Approved Amt' },
                { key: 'status', label: 'Status', render: (row: any) => <Badge variant={row.status === 'APPROVED' ? 'success' : row.status === 'REJECTED' ? 'danger' : 'warning'} label={row.status} /> },
                { key: 'rejectionReason', label: 'Notes' }
              ]} data={data} />
            )}
          </>
        )}
      </div>

      <Modal isOpen={isInvoiceModalOpen} onClose={() => setIsInvoiceModalOpen(false)} title="Create Invoice">
        <form onSubmit={e => { e.preventDefault(); handleAction('/hospital/invoices', invoiceForm, setIsInvoiceModalOpen, 'Invoice created'); }} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Patient ID</label><input className="input" required value={invoiceForm.patientId} onChange={e => setInvoiceForm({...invoiceForm, patientId: e.target.value})} /></div>
          <div className="form-row-2">
            <div className="form-group"><label className="label">Tax (%)</label><input type="number" className="input" required value={invoiceForm.tax} onChange={e => setInvoiceForm({...invoiceForm, tax: +e.target.value})} /></div>
            <div className="form-group"><label className="label">Discount</label><input type="number" className="input" required value={invoiceForm.discount} onChange={e => setInvoiceForm({...invoiceForm, discount: +e.target.value})} /></div>
          </div>
          <button type="submit" className="btn btn-primary">Create Invoice</button>
        </form>
      </Modal>

      <Modal isOpen={isPaymentModalOpen} onClose={() => setIsPaymentModalOpen(false)} title="Process Payment">
        <form onSubmit={e => { e.preventDefault(); handleAction('/hospital/payments', paymentForm, setIsPaymentModalOpen, 'Payment processed'); }} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Invoice ID</label><input className="input" required value={paymentForm.invoiceId} onChange={e => setPaymentForm({...paymentForm, invoiceId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Amount</label><input type="number" className="input" required value={paymentForm.amount} onChange={e => setPaymentForm({...paymentForm, amount: +e.target.value})} /></div>
          <div className="form-group"><label className="label">Method</label>
            <select className="select" required value={paymentForm.paymentMethod} onChange={e => setPaymentForm({...paymentForm, paymentMethod: e.target.value})}>
              <option value="CASH">Cash</option><option value="CREDIT_CARD">Credit Card</option><option value="INSURANCE">Insurance</option>
            </select>
          </div>
          <button type="submit" className="btn btn-primary">Process</button>
        </form>
      </Modal>

      <Modal isOpen={isProviderModalOpen} onClose={() => setIsProviderModalOpen(false)} title="Add Provider">
        <form onSubmit={e => { e.preventDefault(); handleAction('/hospital/insurance/providers', providerForm, setIsProviderModalOpen, 'Provider added'); }} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Name</label><input className="input" required value={providerForm.name} onChange={e => setProviderForm({...providerForm, name: e.target.value})} /></div>
          <div className="form-group"><label className="label">Email</label><input type="email" className="input" required value={providerForm.contactEmail} onChange={e => setProviderForm({...providerForm, contactEmail: e.target.value})} /></div>
          <div className="form-group"><label className="label">Phone</label><input className="input" required value={providerForm.contactPhone} onChange={e => setProviderForm({...providerForm, contactPhone: e.target.value})} /></div>
          <button type="submit" className="btn btn-primary">Add</button>
        </form>
      </Modal>

      <Modal isOpen={isClaimModalOpen} onClose={() => setIsClaimModalOpen(false)} title="Submit Claim">
        <form onSubmit={e => { e.preventDefault(); handleAction('/hospital/insurance/claims', claimForm, setIsClaimModalOpen, 'Claim submitted'); }} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Patient ID</label><input className="input" required value={claimForm.patientId} onChange={e => setClaimForm({...claimForm, patientId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Invoice ID</label><input className="input" required value={claimForm.invoiceId} onChange={e => setClaimForm({...claimForm, invoiceId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Claim Amount</label><input type="number" className="input" required value={claimForm.claimAmount} onChange={e => setClaimForm({...claimForm, claimAmount: +e.target.value})} /></div>
          <button type="submit" className="btn btn-primary">Submit</button>
        </form>
      </Modal>

    </div>
  );
}
