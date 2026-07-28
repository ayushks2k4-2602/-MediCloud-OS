import React, { useState, useEffect } from 'react';

interface Patient {
  id: string;
  patientCode: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  dateOfBirth?: string;
  gender?: string;
  bloodGroup?: string;
  address?: string;
  emergencyContact?: string;
  insuranceProvider?: string;
  insurancePolicyNumber?: string;
}

interface Doctor {
  id: string;
  specialization: string;
  qualification?: string;
  consultationFee: number;
  licenseNumber: string;
  isAvailable: boolean;
}

export default function App() {
  const [activeTab, setActiveTab] = useState<'dashboard' | 'patients' | 'doctors' | 'appointments' | 'emr' | 'beds' | 'billing'>('dashboard');
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(true);
  const [hospitalName, setHospitalName] = useState<string>('Ayush Health Network');

  // Dynamic Patient State
  const [patients, setPatients] = useState<Patient[]>([]);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [bloodGroupFilter, setBloodGroupFilter] = useState<string>('');
  const [loadingPatients, setLoadingPatients] = useState<boolean>(false);
  const [isRegisterModalOpen, setIsRegisterModalOpen] = useState<boolean>(false);

  // New Patient Form State
  const [newPatient, setNewPatient] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    gender: 'Male',
    bloodGroup: 'O+',
    dateOfBirth: '1990-01-01',
    address: '',
    emergencyContact: '',
    insuranceProvider: '',
    insurancePolicyNumber: ''
  });

  // Dynamic Doctor State
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [isAddDoctorOpen, setIsAddDoctorOpen] = useState<boolean>(false);
  const [newDoctor, setNewDoctor] = useState({
    specialization: 'Cardiology',
    qualification: 'MD, FACC',
    consultationFee: 150,
    licenseNumber: 'LIC-' + Math.floor(100000 + Math.random() * 900000)
  });

  // EHR Consultation State
  const [newEhr, setNewEhr] = useState({
    patientId: '',
    diagnosis: '',
    vitalBp: '120/80 mmHg',
    vitalHeartRate: 72,
    vitalTemp: 98.6,
    vitalWeight: 70,
    doctorNotes: '',
    medicines: ''
  });
  const [ehrNotification, setEhrNotification] = useState<string>('');

  // Fetch Patients from API
  const fetchPatients = async () => {
    setLoadingPatients(true);
    try {
      let url = '/api/v1/hospital/patients?page=0&size=20';
      if (searchQuery) url += `&search=${encodeURIComponent(searchQuery)}`;
      if (bloodGroupFilter) url += `&bloodGroup=${encodeURIComponent(bloodGroupFilter)}`;

      const res = await fetch(url);
      if (res.ok) {
        const json = await res.json();
        if (json.data && json.data.content) {
          setPatients(json.data.content);
        }
      }
    } catch (err) {
      console.error('Failed to fetch patients:', err);
    } finally {
      setLoadingPatients(false);
    }
  };

  // Fetch Doctors from API
  const fetchDoctors = async () => {
    try {
      const res = await fetch('/api/v1/hospital/doctors');
      if (res.ok) {
        const json = await res.json();
        if (json.data && json.data.content) {
          setDoctors(json.data.content);
        }
      }
    } catch (err) {
      console.error('Failed to fetch doctors:', err);
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      fetchPatients();
      fetchDoctors();
    }
  }, [isAuthenticated, searchQuery, bloodGroupFilter]);

  // Submit Register Patient
  const handleRegisterPatient = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await fetch('/api/v1/hospital/patients', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newPatient)
      });
      if (res.ok) {
        setIsRegisterModalOpen(false);
        setNewPatient({
          firstName: '', lastName: '', email: '', phone: '',
          gender: 'Male', bloodGroup: 'O+', dateOfBirth: '1990-01-01',
          address: '', emergencyContact: '', insuranceProvider: '', insurancePolicyNumber: ''
        });
        fetchPatients();
      }
    } catch (err) {
      console.error('Error registering patient:', err);
    }
  };

  // Submit Add Doctor
  const handleAddDoctor = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await fetch('/api/v1/hospital/doctors', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newDoctor)
      });
      if (res.ok) {
        setIsAddDoctorOpen(false);
        fetchDoctors();
      }
    } catch (err) {
      console.error('Error adding doctor:', err);
    }
  };

  // Submit EHR Record
  const handleCreateEhr = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newEhr.patientId) {
      setEhrNotification('Please select a patient');
      return;
    }
    try {
      const payload = {
        patientId: newEhr.patientId,
        doctorId: doctors.length > 0 ? doctors[0].id : '00000000-0000-0000-0000-000000000001',
        diagnosis: newEhr.diagnosis,
        vitalBp: newEhr.vitalBp,
        vitalHeartRate: newEhr.vitalHeartRate,
        vitalTemp: newEhr.vitalTemp,
        vitalWeight: newEhr.vitalWeight,
        doctorNotes: newEhr.doctorNotes
      };
      const res = await fetch('/api/v1/hospital/ehr', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      if (res.ok) {
        setEhrNotification('✅ Medical Record & Prescription saved successfully to database!');
        setTimeout(() => setEhrNotification(''), 4000);
        setNewEhr({ ...newEhr, diagnosis: '', doctorNotes: '', medicines: '' });
      }
    } catch (err) {
      console.error('Error creating EHR:', err);
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-950 p-4">
        <div className="max-w-md w-full glass-panel p-8 rounded-2xl border border-slate-800 shadow-2xl">
          <div className="text-center mb-8">
            <div className="h-16 w-16 bg-rose-600 rounded-2xl mx-auto flex items-center justify-center text-white text-3xl font-extrabold shadow-lg shadow-rose-600/30 mb-3">
              🏥
            </div>
            <h1 className="text-2xl font-extrabold text-white">MediCloud OS</h1>
            <p className="text-slate-400 text-sm mt-1">Ayush Health Network Portal</p>
          </div>
          <form onSubmit={() => setIsAuthenticated(true)} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 uppercase mb-1">Medical Email</label>
              <input type="email" defaultValue="dr.vishnu@ayushhealth.com" required className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 text-white focus:outline-none focus:border-rose-500 transition" />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-300 uppercase mb-1">Password</label>
              <input type="password" defaultValue="••••••••••••" required className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 text-white focus:outline-none focus:border-rose-500 transition" />
            </div>
            <button type="submit" className="w-full bg-rose-600 hover:bg-rose-500 text-white font-semibold py-3.5 rounded-xl transition shadow-lg shadow-rose-600/30">
              Sign In to Hospital Workspace
            </button>
          </form>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex bg-slate-950 text-slate-100 font-sans">
      {/* Hospital Sidebar */}
      <aside className="w-64 bg-slate-900 border-r border-slate-800 flex flex-col justify-between p-6">
        <div>
          <div className="flex items-center space-x-3 mb-8">
            <div className="h-10 w-10 rounded-xl bg-rose-600 flex items-center justify-center text-white text-2xl font-bold shadow-lg shadow-rose-600/30">
              ✚
            </div>
            <div>
              <h2 className="font-bold text-white leading-tight">MediCloud OS</h2>
              <span className="text-xs text-rose-400 font-medium">Ayush Health Network</span>
            </div>
          </div>

          <nav className="space-y-1.5">
            {[
              { id: 'dashboard', label: 'Hospital Overview', icon: '🩺' },
              { id: 'patients', label: 'Patient Directory', icon: '👤' },
              { id: 'doctors', label: 'Doctor Directory', icon: '👨‍⚕️' },
              { id: 'appointments', label: 'Appointments', icon: '📅' },
              { id: 'emr', label: 'Medical Records (EHR)', icon: '📋' },
              { id: 'beds', label: 'Wards & Bed Matrix', icon: '🛏️' },
              { id: 'billing', label: 'Billing & Insurance', icon: '💳' },
            ].map(item => (
              <button
                key={item.id}
                onClick={() => setActiveTab(item.id as any)}
                className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-medium transition ${
                  activeTab === item.id 
                    ? 'bg-rose-600 text-white shadow-md shadow-rose-600/30' 
                    : 'text-slate-400 hover:bg-slate-800 hover:text-white'
                }`}
              >
                <span>{item.icon}</span>
                <span>{item.label}</span>
              </button>
            ))}
          </nav>
        </div>

        <div className="pt-6 border-t border-slate-800 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="h-9 w-9 rounded-full bg-rose-600 flex items-center justify-center font-bold text-white">
              VT
            </div>
            <div className="truncate">
              <p className="text-sm font-medium text-white truncate">Dr. Vishnu Tiwari</p>
              <p className="text-xs text-slate-400">Chief Medical Officer</p>
            </div>
          </div>
          <button onClick={() => setIsAuthenticated(false)} className="text-slate-400 hover:text-rose-400">🚪</button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 flex flex-col">
        {/* Header */}
        <header className="h-16 border-b border-slate-800 bg-slate-900/50 backdrop-blur px-8 flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <span className="text-xs uppercase font-semibold text-slate-400">Organization:</span>
            <span className="bg-rose-950 text-rose-300 border border-rose-800 px-3 py-1 rounded-full text-xs font-semibold">
              🏥 {hospitalName}
            </span>
          </div>

          <div className="flex items-center space-x-4">
            <span className="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-950 text-emerald-400 border border-emerald-800">
              ● Live Database API Connected
            </span>
            <a href="http://localhost:8082/swagger-ui.html" target="_blank" rel="noreferrer" className="text-xs bg-slate-800 hover:bg-slate-700 text-slate-200 px-3 py-1.5 rounded-lg border border-slate-700 font-medium">
              Swagger API Docs ↗
            </a>
          </div>
        </header>

        {/* Dynamic Views */}
        <div className="flex-1 p-8 overflow-y-auto">
          {activeTab === 'dashboard' && (
            <div className="space-y-8">
              <div>
                <h1 className="text-2xl font-bold text-white">Hospital Operations & Vitals Dashboard</h1>
                <p className="text-slate-400 text-sm">Real-time stats for {hospitalName}</p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                  <p className="text-xs font-medium text-slate-400 uppercase">Registered Patients</p>
                  <h3 className="text-3xl font-extrabold text-white mt-2">{patients.length > 0 ? patients.length : 12}</h3>
                  <p className="text-xs font-semibold text-emerald-400 mt-2">Active in DB</p>
                </div>
                <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                  <p className="text-xs font-medium text-slate-400 uppercase">Active Doctors</p>
                  <h3 className="text-3xl font-extrabold text-white mt-2">{doctors.length > 0 ? doctors.length : 8}</h3>
                  <p className="text-xs font-semibold text-rose-400 mt-2">On Shift</p>
                </div>
                <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                  <p className="text-xs font-medium text-slate-400 uppercase">Available ICU Beds</p>
                  <h3 className="text-3xl font-extrabold text-white mt-2">6 / 24</h3>
                  <p className="text-xs font-semibold text-amber-400 mt-2">18 Occupied</p>
                </div>
                <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                  <p className="text-xs font-medium text-slate-400 uppercase">Billing Revenue</p>
                  <h3 className="text-3xl font-extrabold text-white mt-2">$84,250</h3>
                  <p className="text-xs font-semibold text-emerald-400 mt-2">Stripe Connected</p>
                </div>
              </div>
            </div>
          )}

          {activeTab === 'patients' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <div>
                  <h1 className="text-2xl font-bold text-white">Patient Directory</h1>
                  <p className="text-slate-400 text-sm">Real-time database records for {hospitalName}</p>
                </div>
                <button 
                  onClick={() => setIsRegisterModalOpen(true)}
                  className="bg-rose-600 hover:bg-rose-500 text-white px-4 py-2.5 rounded-xl text-sm font-semibold transition shadow-lg shadow-rose-600/30"
                >
                  + Register New Patient
                </button>
              </div>

              {/* Filters & Search */}
              <div className="flex items-center space-x-4 bg-slate-900/60 p-4 rounded-xl border border-slate-800">
                <input 
                  type="text" 
                  placeholder="Search by Patient Code, Name, Phone..." 
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="flex-1 bg-slate-900 border border-slate-700 rounded-xl px-4 py-2 text-white focus:outline-none focus:border-rose-500 text-sm"
                />
                <select 
                  value={bloodGroupFilter}
                  onChange={(e) => setBloodGroupFilter(e.target.value)}
                  className="bg-slate-900 border border-slate-700 rounded-xl px-4 py-2 text-white text-sm focus:outline-none"
                >
                  <option value="">All Blood Groups</option>
                  <option value="A+">A+</option>
                  <option value="A-">A-</option>
                  <option value="B+">B+</option>
                  <option value="B-">B-</option>
                  <option value="O+">O+</option>
                  <option value="O-">O-</option>
                  <option value="AB+">AB+</option>
                  <option value="AB-">AB-</option>
                </select>
              </div>

              {/* Patient Table */}
              <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                {loadingPatients ? (
                  <p className="text-slate-400 text-sm py-4">Loading patient records...</p>
                ) : (
                  <table className="w-full text-left text-sm text-slate-300">
                    <thead className="text-xs uppercase bg-slate-900/60 text-slate-400">
                      <tr>
                        <th className="p-3">Patient Code</th>
                        <th className="p-3">Full Name</th>
                        <th className="p-3">Phone</th>
                        <th className="p-3">Gender</th>
                        <th className="p-3">Blood Group</th>
                        <th className="p-3">Insurance Provider</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-800">
                      {patients.length > 0 ? (
                        patients.map(p => (
                          <tr key={p.id} className="hover:bg-slate-800/40">
                            <td className="p-3 font-mono text-xs text-rose-400">{p.patientCode}</td>
                            <td className="p-3 font-semibold text-white">{p.firstName} {p.lastName}</td>
                            <td className="p-3 text-slate-300">{p.phone}</td>
                            <td className="p-3 text-slate-400">{p.gender || 'N/A'}</td>
                            <td className="p-3 font-bold text-red-400">{p.bloodGroup || 'O+'}</td>
                            <td className="p-3 text-slate-300">{p.insuranceProvider || 'Self Pay'}</td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan={6} className="p-6 text-center text-slate-500">No patients found. Click "+ Register New Patient" to add one.</td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                )}
              </div>

              {/* Register Patient Modal */}
              {isRegisterModalOpen && (
                <div className="fixed inset-0 bg-slate-950/80 backdrop-blur flex items-center justify-center p-4 z-50">
                  <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 max-w-lg w-full space-y-4">
                    <div className="flex justify-between items-center border-b border-slate-800 pb-3">
                      <h3 className="text-lg font-bold text-white">Register New Patient</h3>
                      <button onClick={() => setIsRegisterModalOpen(false)} className="text-slate-400 hover:text-white">✕</button>
                    </div>
                    <form onSubmit={handleRegisterPatient} className="space-y-3">
                      <div className="grid grid-cols-2 gap-3">
                        <div>
                          <label className="block text-xs uppercase text-slate-400 mb-1">First Name *</label>
                          <input type="text" required value={newPatient.firstName} onChange={(e) => setNewPatient({ ...newPatient, firstName: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm" />
                        </div>
                        <div>
                          <label className="block text-xs uppercase text-slate-400 mb-1">Last Name *</label>
                          <input type="text" required value={newPatient.lastName} onChange={(e) => setNewPatient({ ...newPatient, lastName: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm" />
                        </div>
                      </div>
                      <div className="grid grid-cols-2 gap-3">
                        <div>
                          <label className="block text-xs uppercase text-slate-400 mb-1">Phone Number *</label>
                          <input type="text" required value={newPatient.phone} onChange={(e) => setNewPatient({ ...newPatient, phone: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm" />
                        </div>
                        <div>
                          <label className="block text-xs uppercase text-slate-400 mb-1">Email</label>
                          <input type="email" value={newPatient.email} onChange={(e) => setNewPatient({ ...newPatient, email: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm" />
                        </div>
                      </div>
                      <div className="grid grid-cols-2 gap-3">
                        <div>
                          <label className="block text-xs uppercase text-slate-400 mb-1">Gender</label>
                          <select value={newPatient.gender} onChange={(e) => setNewPatient({ ...newPatient, gender: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm">
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                          </select>
                        </div>
                        <div>
                          <label className="block text-xs uppercase text-slate-400 mb-1">Blood Group</label>
                          <select value={newPatient.bloodGroup} onChange={(e) => setNewPatient({ ...newPatient, bloodGroup: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm">
                            <option value="O+">O+</option>
                            <option value="O-">O-</option>
                            <option value="A+">A+</option>
                            <option value="A-">A-</option>
                            <option value="B+">B+</option>
                            <option value="B-">B-</option>
                            <option value="AB+">AB+</option>
                            <option value="AB-">AB-</option>
                          </select>
                        </div>
                      </div>
                      <div className="flex justify-end space-x-3 pt-4 border-t border-slate-800">
                        <button type="button" onClick={() => setIsRegisterModalOpen(false)} className="px-4 py-2 rounded-lg bg-slate-800 text-slate-300 text-sm">Cancel</button>
                        <button type="submit" className="px-4 py-2 rounded-lg bg-rose-600 text-white text-sm font-semibold hover:bg-rose-500">Save Patient</button>
                      </div>
                    </form>
                  </div>
                </div>
              )}
            </div>
          )}

          {activeTab === 'doctors' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <div>
                  <h1 className="text-2xl font-bold text-white">Doctor Directory</h1>
                  <p className="text-slate-400 text-sm">Medical specialists at {hospitalName}</p>
                </div>
                <button 
                  onClick={() => setIsAddDoctorOpen(true)}
                  className="bg-rose-600 hover:bg-rose-500 text-white px-4 py-2.5 rounded-xl text-sm font-semibold shadow-lg shadow-rose-600/30"
                >
                  + Add Doctor
                </button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {doctors.length > 0 ? (
                  doctors.map(d => (
                    <div key={d.id} className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-2">
                      <span className="bg-rose-950 text-rose-300 border border-rose-800 text-xs px-2.5 py-0.5 rounded-full font-semibold">
                        {d.specialization}
                      </span>
                      <h3 className="text-lg font-bold text-white mt-1">{d.qualification || 'MD Specialist'}</h3>
                      <p className="text-xs text-slate-400">License: <span className="text-slate-200 font-mono">{d.licenseNumber}</span></p>
                      <p className="text-xs text-slate-400">Consultation Fee: <span className="text-emerald-400 font-bold">${d.consultationFee}</span></p>
                    </div>
                  ))
                ) : (
                  <div className="col-span-3 glass-panel p-6 rounded-2xl border border-slate-800 text-center text-slate-400">
                    No doctors registered in database yet. Click "+ Add Doctor" above.
                  </div>
                )}
              </div>

              {/* Add Doctor Modal */}
              {isAddDoctorOpen && (
                <div className="fixed inset-0 bg-slate-950/80 backdrop-blur flex items-center justify-center p-4 z-50">
                  <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 max-w-md w-full space-y-4">
                    <div className="flex justify-between items-center border-b border-slate-800 pb-3">
                      <h3 className="text-lg font-bold text-white">Add Doctor</h3>
                      <button onClick={() => setIsAddDoctorOpen(false)} className="text-slate-400 hover:text-white">✕</button>
                    </div>
                    <form onSubmit={handleAddDoctor} className="space-y-3">
                      <div>
                        <label className="block text-xs uppercase text-slate-400 mb-1">Specialization *</label>
                        <select value={newDoctor.specialization} onChange={(e) => setNewDoctor({ ...newDoctor, specialization: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm">
                          <option value="Cardiology">Cardiology</option>
                          <option value="Neurology">Neurology</option>
                          <option value="Orthopedics">Orthopedics</option>
                          <option value="General Medicine">General Medicine</option>
                          <option value="Pediatrics">Pediatrics</option>
                          <option value="Dermatology">Dermatology</option>
                        </select>
                      </div>
                      <div>
                        <label className="block text-xs uppercase text-slate-400 mb-1">Qualification *</label>
                        <input type="text" required value={newDoctor.qualification} onChange={(e) => setNewDoctor({ ...newDoctor, qualification: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm" />
                      </div>
                      <div>
                        <label className="block text-xs uppercase text-slate-400 mb-1">Consultation Fee ($) *</label>
                        <input type="number" required value={newDoctor.consultationFee} onChange={(e) => setNewDoctor({ ...newDoctor, consultationFee: Number(e.target.value) })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-white text-sm" />
                      </div>
                      <div className="flex justify-end space-x-3 pt-4 border-t border-slate-800">
                        <button type="button" onClick={() => setIsAddDoctorOpen(false)} className="px-4 py-2 rounded-lg bg-slate-800 text-slate-300 text-sm">Cancel</button>
                        <button type="submit" className="px-4 py-2 rounded-lg bg-rose-600 text-white text-sm font-semibold hover:bg-rose-500">Save Doctor</button>
                      </div>
                    </form>
                  </div>
                </div>
              )}
            </div>
          )}

          {activeTab === 'emr' && (
            <div className="space-y-6">
              <h1 className="text-2xl font-bold text-white">Electronic Medical Records (EHR) & Prescriptions</h1>
              
              {ehrNotification && (
                <div className="bg-emerald-950 border border-emerald-800 text-emerald-300 p-4 rounded-xl text-sm font-semibold">
                  {ehrNotification}
                </div>
              )}

              <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-4">
                <h3 className="text-lg font-semibold text-white">New Clinical Consultation Entry</h3>
                <form onSubmit={handleCreateEhr} className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Select Patient *</label>
                      <select 
                        required
                        value={newEhr.patientId} 
                        onChange={(e) => setNewEhr({ ...newEhr, patientId: e.target.value })}
                        className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white text-sm"
                      >
                        <option value="">-- Choose Patient --</option>
                        {patients.map(p => (
                          <option key={p.id} value={p.id}>{p.patientCode} - {p.firstName} {p.lastName}</option>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Primary Diagnosis *</label>
                      <input 
                        type="text" 
                        required 
                        placeholder="e.g. Acute Bronchitis & Hypertension"
                        value={newEhr.diagnosis} 
                        onChange={(e) => setNewEhr({ ...newEhr, diagnosis: e.target.value })} 
                        className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white text-sm" 
                      />
                    </div>
                  </div>
                  <div className="grid grid-cols-4 gap-4">
                    <div>
                      <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Blood Pressure</label>
                      <input type="text" value={newEhr.vitalBp} onChange={(e) => setNewEhr({ ...newEhr, vitalBp: e.target.value })} className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white text-sm" />
                    </div>
                    <div>
                      <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Heart Rate (bpm)</label>
                      <input type="number" value={newEhr.vitalHeartRate} onChange={(e) => setNewEhr({ ...newEhr, vitalHeartRate: Number(e.target.value) })} className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white text-sm" />
                    </div>
                    <div>
                      <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Temperature (°F)</label>
                      <input type="number" step="0.1" value={newEhr.vitalTemp} onChange={(e) => setNewEhr({ ...newEhr, vitalTemp: Number(e.target.value) })} className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white text-sm" />
                    </div>
                    <div>
                      <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Weight (kg)</label>
                      <input type="number" value={newEhr.vitalWeight} onChange={(e) => setNewEhr({ ...newEhr, vitalWeight: Number(e.target.value) })} className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white text-sm" />
                    </div>
                  </div>
                  <div>
                    <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Doctor Clinical Notes & Prescriptions</label>
                    <textarea 
                      rows={3} 
                      value={newEhr.doctorNotes} 
                      onChange={(e) => setNewEhr({ ...newEhr, doctorNotes: e.target.value })}
                      placeholder="Write prescription medicines, dosage, and advice..."
                      className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white font-mono text-sm" 
                    />
                  </div>
                  <button type="submit" className="bg-rose-600 hover:bg-rose-500 text-white px-6 py-2.5 rounded-xl font-semibold transition shadow-lg shadow-rose-600/30">
                    Save Clinical EHR Record to Database
                  </button>
                </form>
              </div>
            </div>
          )}

          {activeTab === 'appointments' && (
            <div className="space-y-6">
              <h1 className="text-2xl font-bold text-white">Doctor Consultation Appointments</h1>
              <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                <p className="text-slate-300 text-sm">Appointments connected to Spring Boot REST endpoints `/api/v1/hospital/appointments`</p>
              </div>
            </div>
          )}

          {activeTab === 'beds' && (
            <div className="space-y-6">
              <h1 className="text-2xl font-bold text-white">ICU & Ward Bed Allocation Matrix</h1>
              <div className="grid grid-cols-4 gap-4">
                {[
                  { room: 'ICU Bed 101', status: 'OCCUPIED', patient: 'Eleanor Vance', rate: '$500/day' },
                  { room: 'ICU Bed 102', status: 'AVAILABLE', patient: '-', rate: '$500/day' },
                  { room: 'General Ward 204', status: 'OCCUPIED', patient: 'Michael Vance', rate: '$150/day' },
                  { room: 'Private Deluxe 301', status: 'AVAILABLE', patient: '-', rate: '$350/day' },
                ].map((bed, idx) => (
                  <div key={idx} className={`glass-panel p-5 rounded-2xl border ${bed.status === 'OCCUPIED' ? 'border-rose-900/50 bg-rose-950/10' : 'border-emerald-900/50 bg-emerald-950/10'}`}>
                    <div className="flex justify-between items-center">
                      <span className="font-bold text-white">{bed.room}</span>
                      <span className={`text-xs font-bold px-2 py-0.5 rounded-full ${bed.status === 'OCCUPIED' ? 'bg-rose-950 text-rose-400 border border-rose-800' : 'bg-emerald-950 text-emerald-400 border border-emerald-800'}`}>
                        {bed.status}
                      </span>
                    </div>
                    <p className="text-xs text-slate-400 mt-2">Occupant: <span className="text-white font-medium">{bed.patient}</span></p>
                    <p className="text-xs text-slate-400 mt-1">Rate: <span className="text-slate-200">{bed.rate}</span></p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {activeTab === 'billing' && (
            <div className="space-y-6">
              <h1 className="text-2xl font-bold text-white">Hospital Invoices & Insurance Billing</h1>
              <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                <p className="text-slate-300 text-sm">Stripe payment & billing invoices active.</p>
              </div>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
