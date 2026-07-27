import React, { useState } from 'react';

export default function App() {
  const [activeTab, setActiveTab] = useState<'dashboard' | 'patients' | 'doctors' | 'appointments' | 'emr' | 'beds' | 'billing'>('dashboard');
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(true);
  const [hospitalName, setHospitalName] = useState<string>('St. Jude Medical Center');

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-950 p-4">
        <div className="max-w-md w-full glass-panel p-8 rounded-2xl border border-slate-800 shadow-2xl">
          <div className="text-center mb-8">
            <div className="h-16 w-16 bg-rose-600 rounded-2xl mx-auto flex items-center justify-center text-white text-3xl font-extrabold shadow-lg shadow-rose-600/30 mb-3">
              🏥
            </div>
            <h1 className="text-2xl font-extrabold text-white">MediCloud SaaS Platform</h1>
            <p className="text-slate-400 text-sm mt-1">Multi-Tenant Hospital Operating System</p>
          </div>
          <form onSubmit={() => setIsAuthenticated(true)} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 uppercase mb-1">Medical Email</label>
              <input type="email" defaultValue="dr.alex@stjude.com" required className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 text-white focus:outline-none focus:border-rose-500 transition" />
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
              <span className="text-xs text-rose-400 font-medium">Hospital Management</span>
            </div>
          </div>

          <nav className="space-y-1.5">
            {[
              { id: 'dashboard', label: 'Hospital Overview', icon: '🩺' },
              { id: 'patients', label: 'Patient Directory', icon: '👤' },
              { id: 'doctors', label: 'Doctors & Shifts', icon: '👨‍⚕️' },
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
              DR
            </div>
            <div className="truncate">
              <p className="text-sm font-medium text-white truncate">Dr. Alex Morgan</p>
              <p className="text-xs text-slate-400">Chief Medical Officer</p>
            </div>
          </div>
          <button onClick={() => setIsAuthenticated(false)} className="text-slate-400 hover:text-rose-400">🚪</button>
        </div>
      </aside>

      {/* Main Content Area */}
      <main className="flex-1 flex flex-col">
        {/* Header */}
        <header className="h-16 border-b border-slate-800 bg-slate-900/50 backdrop-blur px-8 flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <span className="text-xs uppercase font-semibold text-slate-400">Hospital Organization:</span>
            <span className="bg-rose-950 text-rose-300 border border-rose-800 px-3 py-1 rounded-full text-xs font-semibold">
              🏥 {hospitalName}
            </span>
          </div>

          <div className="flex items-center space-x-4">
            <span className="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-950 text-emerald-400 border border-emerald-800">
              ● Emergency Vitals Active
            </span>
            <a href="http://localhost:8081/swagger-ui.html" target="_blank" rel="noreferrer" className="text-xs bg-slate-800 hover:bg-slate-700 text-slate-200 px-3 py-1.5 rounded-lg border border-slate-700 font-medium">
              Hospital REST APIs ↗
            </a>
          </div>
        </header>

        {/* Dynamic Body */}
        <div className="flex-1 p-8 overflow-y-auto">
          {activeTab === 'dashboard' && (
            <div className="space-y-8">
              <div>
                <h1 className="text-2xl font-bold text-white">Hospital Operations & Live Vitals</h1>
                <p className="text-slate-400 text-sm">Real-time status for {hospitalName}</p>
              </div>

              {/* Metric Cards */}
              <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                {[
                  { title: 'Admitted Patients', value: '142 Patients', change: '85% Bed Occupancy', color: 'from-rose-600 to-pink-600' },
                  { title: 'Today\'s Appointments', value: '48 Bookings', change: '12 Emergency Cases', color: 'from-blue-600 to-cyan-600' },
                  { title: 'Available ICU Beds', value: '6 / 24 Available', change: '18 Occupied', color: 'from-amber-600 to-orange-600' },
                  { title: 'Hospital Billing Revenue', value: '$84,250', change: '8 Insurance Claims Pending', color: 'from-emerald-600 to-teal-600' },
                ].map((stat, i) => (
                  <div key={i} className="glass-panel p-6 rounded-2xl border border-slate-800">
                    <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">{stat.title}</p>
                    <h3 className="text-2xl font-extrabold text-white mt-2">{stat.value}</h3>
                    <p className="text-xs font-semibold text-rose-400 mt-2">{stat.change}</p>
                  </div>
                ))}
              </div>

              {/* Today's Schedule Table */}
              <div className="glass-panel rounded-2xl border border-slate-800 p-6">
                <h3 className="text-lg font-bold text-white mb-4">Today's Doctor Consultations</h3>
                <table className="w-full text-left text-sm text-slate-300">
                  <thead className="text-xs uppercase bg-slate-900/60 text-slate-400">
                    <tr>
                      <th className="p-3">Time</th>
                      <th className="p-3">Patient Code & Name</th>
                      <th className="p-3">Assigned Doctor</th>
                      <th className="p-3">Department</th>
                      <th className="p-3">Status</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-800">
                    {[
                      { time: '09:00 AM', patient: 'PAT-94021 - Sarah Jenkins', doctor: 'Dr. Robert Chen', dept: 'Cardiology', status: 'CONFIRMED' },
                      { time: '10:30 AM', patient: 'PAT-94028 - Michael Vance', doctor: 'Dr. Emily Watson', dept: 'Neurology', status: 'IN_PROGRESS' },
                      { time: '11:15 AM', patient: 'PAT-94033 - David Ross', doctor: 'Dr. James Miller', dept: 'Orthopedics', status: 'COMPLETED' },
                    ].map((row, idx) => (
                      <tr key={idx} className="hover:bg-slate-800/40">
                        <td className="p-3 text-slate-400 font-mono text-xs">{row.time}</td>
                        <td className="p-3 font-medium text-white">{row.patient}</td>
                        <td className="p-3 text-slate-300">{row.doctor}</td>
                        <td className="p-3"><span className="bg-slate-800 text-slate-300 text-xs px-2.5 py-1 rounded-full font-medium">{row.dept}</span></td>
                        <td className="p-3"><span className="bg-emerald-950 text-emerald-400 border border-emerald-800 text-xs px-2.5 py-0.5 rounded-full font-semibold">{row.status}</span></td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {activeTab === 'patients' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <div>
                  <h1 className="text-2xl font-bold text-white">Patient Directory</h1>
                  <p className="text-slate-400 text-sm">Registered patients across tenant hospital</p>
                </div>
                <button className="bg-rose-600 hover:bg-rose-500 text-white px-4 py-2 rounded-xl text-sm font-semibold">
                  + Register New Patient
                </button>
              </div>

              <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                <table className="w-full text-left text-sm text-slate-300">
                  <thead className="text-xs uppercase bg-slate-900/60 text-slate-400">
                    <tr>
                      <th className="p-3">Patient ID</th>
                      <th className="p-3">Full Name</th>
                      <th className="p-3">Phone</th>
                      <th className="p-3">Blood Group</th>
                      <th className="p-3">Emergency Contact</th>
                      <th className="p-3">Insurance Provider</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-800">
                    <tr className="hover:bg-slate-800/40">
                      <td className="p-3 font-mono text-xs text-rose-400">PAT-88102</td>
                      <td className="p-3 font-semibold text-white">Eleanor Vance</td>
                      <td className="p-3 text-slate-300">+1 (555) 234-5678</td>
                      <td className="p-3 font-bold text-red-400">O+</td>
                      <td className="p-3 text-slate-400">John Vance (Husband)</td>
                      <td className="p-3 text-slate-300">Blue Cross Shield</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {activeTab === 'emr' && (
            <div className="space-y-6">
              <h1 className="text-2xl font-bold text-white">Electronic Medical Records (EHR) & Prescriptions</h1>
              <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-4">
                <h3 className="text-lg font-semibold text-white">New Clinical Consultation Entry</h3>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Select Patient</label>
                    <select className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white">
                      <option>PAT-88102 - Eleanor Vance</option>
                    </select>
                  </div>
                  <div>
                    <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Primary Diagnosis</label>
                    <input type="text" defaultValue="Acute Bronchitis & Hypertension" className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white" />
                  </div>
                </div>
                <div className="grid grid-cols-4 gap-4">
                  <div>
                    <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Blood Pressure</label>
                    <input type="text" defaultValue="128/82 mmHg" className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white" />
                  </div>
                  <div>
                    <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Heart Rate</label>
                    <input type="text" defaultValue="76 bpm" className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white" />
                  </div>
                  <div>
                    <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Temperature</label>
                    <input type="text" defaultValue="98.6 °F" className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white" />
                  </div>
                  <div>
                    <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Weight</label>
                    <input type="text" defaultValue="68 kg" className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white" />
                  </div>
                </div>
                <div>
                  <label className="block text-xs uppercase font-semibold text-slate-400 mb-1">Prescribed Medicines & Dosage</label>
                  <textarea rows={3} defaultValue="1. Amoxicillin 500mg - Twice daily after meals (7 days)&#10;2. Amlodipine 5mg - Once daily in morning" className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white font-mono text-sm" />
                </div>
                <button className="bg-rose-600 hover:bg-rose-500 text-white px-6 py-2.5 rounded-xl font-semibold">
                  Save Clinical Record & Issue Prescription
                </button>
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
              <h1 className="text-2xl font-bold text-white">Hospital Patient Invoices & Insurance Claims</h1>
              <div className="glass-panel p-6 rounded-2xl border border-slate-800">
                <table className="w-full text-left text-sm text-slate-300">
                  <thead className="text-xs uppercase bg-slate-900/60 text-slate-400">
                    <tr>
                      <th className="p-3">Invoice #</th>
                      <th className="p-3">Patient</th>
                      <th className="p-3">Consultation</th>
                      <th className="p-3">Pharmacy & Lab</th>
                      <th className="p-3">Total Amount</th>
                      <th className="p-3">Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr className="border-t border-slate-800">
                      <td className="p-3 font-mono text-xs text-slate-400">INV-2026-0041</td>
                      <td className="p-3 font-semibold text-white">Eleanor Vance</td>
                      <td className="p-3 text-slate-300">$150.00</td>
                      <td className="p-3 text-slate-300">$240.00</td>
                      <td className="p-3 font-bold text-emerald-400">$390.00</td>
                      <td className="p-3"><span className="bg-emerald-950 text-emerald-400 border border-emerald-800 text-xs px-2.5 py-0.5 rounded-full font-semibold">PAID</span></td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
