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

export default function PharmacyPage({ toast }: ToastProps) {
  const [activeTab, setActiveTab] = useState<'medicines' | 'suppliers' | 'fulfillments' | 'stock'>('medicines');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Data states
  const [medicines, setMedicines] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [fulfillments, setFulfillments] = useState([]);
  const [stockMovements, setStockMovements] = useState([]);

  // Modal states
  const [isMedicineModalOpen, setIsMedicineModalOpen] = useState(false);
  const [isSupplierModalOpen, setIsSupplierModalOpen] = useState(false);
  const [isFulfillmentModalOpen, setIsFulfillmentModalOpen] = useState(false);
  const [isStockModalOpen, setIsStockModalOpen] = useState(false);

  // Form states
  const [medicineForm, setMedicineForm] = useState({ name: '', genericName: '', manufacturer: '', category: '', stockQuantity: 0, unitPrice: 0, expiryDate: '' });
  const [supplierForm, setSupplierForm] = useState({ name: '', email: '', phone: '', address: '' });
  const [fulfillmentForm, setFulfillmentForm] = useState({ patientId: '', medicineId: '', prescribedBy: '', quantity: 1, notes: '' });
  const [stockForm, setStockForm] = useState({ medicineId: '', movementType: 'IN', quantity: 1, reference: '' });

  const fetchData = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      if (activeTab === 'medicines') {
        const res = await api.get<any>('/hospital/medicines?page=0&size=20');
        setMedicines(res.data.content || res.data);
      } else if (activeTab === 'suppliers') {
        const res = await api.get<any>('/hospital/pharmacy/suppliers');
        setSuppliers(res.data);
      } else if (activeTab === 'fulfillments') {
        const res = await api.get<any>('/hospital/pharmacy/fulfillments?page=0&size=20');
        setFulfillments(res.data.content || res.data);
      } else if (activeTab === 'stock') {
        const res = await api.get<any>('/hospital/pharmacy/stock-movements?page=0&size=20');
        setStockMovements(res.data.content || res.data);
      }
    } catch (err) {
      setError('Failed to fetch data');
      toast.error('Error fetching data');
    } finally {
      setLoading(false);
    }
  }, [activeTab, toast]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleAddMedicine = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/hospital/medicines', medicineForm);
      toast.success('Medicine added successfully');
      setIsMedicineModalOpen(false);
      fetchData();
    } catch (err) {
      toast.error('Failed to add medicine');
    }
  };

  const handleAddSupplier = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/hospital/pharmacy/suppliers', supplierForm);
      toast.success('Supplier added successfully');
      setIsSupplierModalOpen(false);
      fetchData();
    } catch (err) {
      toast.error('Failed to add supplier');
    }
  };

  const handleFulfillPrescription = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/hospital/pharmacy/fulfillments', fulfillmentForm);
      toast.success('Prescription fulfilled successfully');
      setIsFulfillmentModalOpen(false);
      fetchData();
    } catch (err) {
      toast.error('Failed to fulfill prescription');
    }
  };

  const handleLogStockMovement = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/hospital/pharmacy/stock-movements', stockForm);
      toast.success('Stock movement logged successfully');
      setIsStockModalOpen(false);
      fetchData();
    } catch (err) {
      toast.error('Failed to log stock movement');
    }
  };

  return (
    <div className="animate-fade-in">
      <header className="page-header flex flex-between">
        <div>
          <h1 className="page-title">Pharmacy Management</h1>
          <p className="page-subtitle">Manage medicines, suppliers, stock, and fulfillments.</p>
        </div>
        <div className="flex gap-4">
          <button className={`btn ${activeTab === 'medicines' ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActiveTab('medicines')}>Medicines</button>
          <button className={`btn ${activeTab === 'suppliers' ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActiveTab('suppliers')}>Suppliers</button>
          <button className={`btn ${activeTab === 'fulfillments' ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActiveTab('fulfillments')}>Fulfillments</button>
          <button className={`btn ${activeTab === 'stock' ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActiveTab('stock')}>Stock</button>
        </div>
      </header>

      {error && <div className="badge badge-danger">{error}</div>}

      <div className="mt-6">
        {loading ? (
          <div>Loading...</div>
        ) : activeTab === 'medicines' ? (
          <div className="card">
            <div className="flex flex-between mb-4">
              <h2 className="section-title">Medicines</h2>
              <button className="btn btn-primary" onClick={() => setIsMedicineModalOpen(true)}>+ Add Medicine</button>
            </div>
            {medicines.length === 0 ? <EmptyState title="No medicines found" /> : (
              <DataTable 
                columns={[
                  { key: 'name', label: 'Name' },
                  { key: 'genericName', label: 'Generic Name' },
                  { key: 'manufacturer', label: 'Manufacturer' },
                  { key: 'category', label: 'Category' },
                  { key: 'stockQuantity', label: 'Stock' },
                  { key: 'unitPrice', label: 'Unit Price' },
                  { key: 'expiryDate', label: 'Expiry Date' }
                ]}
                data={medicines}
              />
            )}
          </div>
        ) : activeTab === 'suppliers' ? (
          <div className="card">
            <div className="flex flex-between mb-4">
              <h2 className="section-title">Suppliers</h2>
              <button className="btn btn-primary" onClick={() => setIsSupplierModalOpen(true)}>+ Add Supplier</button>
            </div>
            {suppliers.length === 0 ? <EmptyState title="No suppliers found" /> : (
              <div className="grid grid-cols-3 gap-4">
                {suppliers.map((s: any) => (
                  <div key={s.id} className="stat-card">
                    <h3 className="stat-label">{s.name}</h3>
                    <div className="stat-value text-sm">{s.email}</div>
                    <div className="text-sm">{s.phone}</div>
                    <div className="text-sm">{s.address}</div>
                  </div>
                ))}
              </div>
            )}
          </div>
        ) : activeTab === 'fulfillments' ? (
          <div className="card">
            <div className="flex flex-between mb-4">
              <h2 className="section-title">Fulfillments</h2>
              <button className="btn btn-primary" onClick={() => setIsFulfillmentModalOpen(true)}>+ Fulfill Prescription</button>
            </div>
            {fulfillments.length === 0 ? <EmptyState title="No fulfillments found" /> : (
              <DataTable 
                columns={[
                  { key: 'patientId', label: 'Patient ID' },
                  { key: 'medicineId', label: 'Medicine ID' },
                  { key: 'prescribedBy', label: 'Prescribed By' },
                  { key: 'quantity', label: 'Qty' },
                  { key: 'dispensedAt', label: 'Dispensed At' },
                  { key: 'notes', label: 'Notes' }
                ]}
                data={fulfillments}
              />
            )}
          </div>
        ) : (
          <div className="card">
            <div className="flex flex-between mb-4">
              <h2 className="section-title">Stock Movements</h2>
              <button className="btn btn-primary" onClick={() => setIsStockModalOpen(true)}>+ Log Movement</button>
            </div>
            {stockMovements.length === 0 ? <EmptyState title="No stock movements found" /> : (
              <DataTable 
                columns={[
                  { key: 'medicineId', label: 'Medicine ID' },
                  { key: 'movementType', label: 'Type', render: (row: any) => <Badge variant={row.movementType === 'IN' ? 'success' : 'danger'} label={row.movementType} /> },
                  { key: 'quantity', label: 'Qty' },
                  { key: 'reference', label: 'Reference' }
                ]}
                data={stockMovements}
              />
            )}
          </div>
        )}
      </div>

      <Modal isOpen={isMedicineModalOpen} onClose={() => setIsMedicineModalOpen(false)} title="Add Medicine">
        <form onSubmit={handleAddMedicine} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Name</label><input className="input" required value={medicineForm.name} onChange={e => setMedicineForm({...medicineForm, name: e.target.value})} /></div>
          <div className="form-group"><label className="label">Generic Name</label><input className="input" required value={medicineForm.genericName} onChange={e => setMedicineForm({...medicineForm, genericName: e.target.value})} /></div>
          <div className="form-group"><label className="label">Manufacturer</label><input className="input" required value={medicineForm.manufacturer} onChange={e => setMedicineForm({...medicineForm, manufacturer: e.target.value})} /></div>
          <div className="form-group"><label className="label">Category</label><input className="input" required value={medicineForm.category} onChange={e => setMedicineForm({...medicineForm, category: e.target.value})} /></div>
          <div className="form-row-2">
            <div className="form-group"><label className="label">Stock Qty</label><input type="number" className="input" required value={medicineForm.stockQuantity} onChange={e => setMedicineForm({...medicineForm, stockQuantity: +e.target.value})} /></div>
            <div className="form-group"><label className="label">Unit Price</label><input type="number" className="input" required value={medicineForm.unitPrice} onChange={e => setMedicineForm({...medicineForm, unitPrice: +e.target.value})} /></div>
          </div>
          <div className="form-group"><label className="label">Expiry Date</label><input type="date" className="input" required value={medicineForm.expiryDate} onChange={e => setMedicineForm({...medicineForm, expiryDate: e.target.value})} /></div>
          <button type="submit" className="btn btn-primary">Add Medicine</button>
        </form>
      </Modal>

      <Modal isOpen={isSupplierModalOpen} onClose={() => setIsSupplierModalOpen(false)} title="Add Supplier">
        <form onSubmit={handleAddSupplier} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Name</label><input className="input" required value={supplierForm.name} onChange={e => setSupplierForm({...supplierForm, name: e.target.value})} /></div>
          <div className="form-group"><label className="label">Email</label><input type="email" className="input" required value={supplierForm.email} onChange={e => setSupplierForm({...supplierForm, email: e.target.value})} /></div>
          <div className="form-group"><label className="label">Phone</label><input className="input" required value={supplierForm.phone} onChange={e => setSupplierForm({...supplierForm, phone: e.target.value})} /></div>
          <div className="form-group"><label className="label">Address</label><textarea className="textarea" required value={supplierForm.address} onChange={e => setSupplierForm({...supplierForm, address: e.target.value})} /></div>
          <button type="submit" className="btn btn-primary">Add Supplier</button>
        </form>
      </Modal>

      <Modal isOpen={isFulfillmentModalOpen} onClose={() => setIsFulfillmentModalOpen(false)} title="Fulfill Prescription">
        <form onSubmit={handleFulfillPrescription} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Patient ID</label><input className="input" required value={fulfillmentForm.patientId} onChange={e => setFulfillmentForm({...fulfillmentForm, patientId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Medicine ID</label><input className="input" required value={fulfillmentForm.medicineId} onChange={e => setFulfillmentForm({...fulfillmentForm, medicineId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Prescribed By</label><input className="input" required value={fulfillmentForm.prescribedBy} onChange={e => setFulfillmentForm({...fulfillmentForm, prescribedBy: e.target.value})} /></div>
          <div className="form-group"><label className="label">Quantity</label><input type="number" className="input" required value={fulfillmentForm.quantity} onChange={e => setFulfillmentForm({...fulfillmentForm, quantity: +e.target.value})} /></div>
          <div className="form-group"><label className="label">Notes</label><textarea className="textarea" value={fulfillmentForm.notes} onChange={e => setFulfillmentForm({...fulfillmentForm, notes: e.target.value})} /></div>
          <button type="submit" className="btn btn-primary">Fulfill Prescription</button>
        </form>
      </Modal>

      <Modal isOpen={isStockModalOpen} onClose={() => setIsStockModalOpen(false)} title="Log Stock Movement">
        <form onSubmit={handleLogStockMovement} className="flex flex-col gap-4">
          <div className="form-group"><label className="label">Medicine ID</label><input className="input" required value={stockForm.medicineId} onChange={e => setStockForm({...stockForm, medicineId: e.target.value})} /></div>
          <div className="form-group"><label className="label">Movement Type</label>
            <select className="select" required value={stockForm.movementType} onChange={e => setStockForm({...stockForm, movementType: e.target.value})}>
              <option value="IN">Stock In</option>
              <option value="OUT">Stock Out</option>
            </select>
          </div>
          <div className="form-group"><label className="label">Quantity</label><input type="number" className="input" required value={stockForm.quantity} onChange={e => setStockForm({...stockForm, quantity: +e.target.value})} /></div>
          <div className="form-group"><label className="label">Reference</label><input className="input" required value={stockForm.reference} onChange={e => setStockForm({...stockForm, reference: e.target.value})} /></div>
          <button type="submit" className="btn btn-primary">Log Movement</button>
        </form>
      </Modal>
    </div>
  );
}
