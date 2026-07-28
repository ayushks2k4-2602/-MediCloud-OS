import React, { lazy, Suspense } from 'react';
import { useAuth, useToast } from './hooks';
import Layout from './components/Layout';
import Toast from './components/Toast';
import LoginPage from './pages/LoginPage';

// Lazy-load page modules for performance
const DashboardPage = lazy(() => import('./pages/DashboardPage'));
const PatientsPage = lazy(() => import('./pages/PatientsPage'));
const DoctorsPage = lazy(() => import('./pages/DoctorsPage'));
const AppointmentsPage = lazy(() => import('./pages/AppointmentsPage'));
const EhrPage = lazy(() => import('./pages/EhrPage'));
const LaboratoryPage = lazy(() => import('./pages/LaboratoryPage'));
const PharmacyPage = lazy(() => import('./pages/PharmacyPage'));
const RadiologyPage = lazy(() => import('./pages/RadiologyPage'));
const WardsPage = lazy(() => import('./pages/WardsPage'));
const BillingPage = lazy(() => import('./pages/BillingPage'));
const AuditPage = lazy(() => import('./pages/AuditPage'));

function PageLoader() {
  return (
    <div className="flex-center" style={{ padding: '4rem' }}>
      <div className="spinner" aria-label="Loading page" />
    </div>
  );
}

export default function App() {
  const auth = useAuth();
  const toast = useToast();
  const [activeTab, setActiveTab] = React.useState('dashboard');

  if (!auth.isAuthenticated) {
    return (
      <>
        <LoginPage onLogin={auth.login} />
        <Toast toasts={toast.toasts} onRemove={toast.removeToast} />
      </>
    );
  }

  const toastActions = {
    success: toast.success,
    error: toast.error,
    info: toast.info,
    warning: toast.warning,
  };

  function renderPage() {
    switch (activeTab) {
      case 'dashboard':
        return <DashboardPage />;
      case 'patients':
        return <PatientsPage toast={toastActions} />;
      case 'doctors':
        return <DoctorsPage toast={toastActions} />;
      case 'appointments':
        return <AppointmentsPage toast={toastActions} />;
      case 'ehr':
        return <EhrPage toast={toastActions} />;
      case 'laboratory':
        return <LaboratoryPage toast={toastActions} />;
      case 'pharmacy':
        return <PharmacyPage toast={toastActions} />;
      case 'radiology':
        return <RadiologyPage toast={toastActions} />;
      case 'wards':
        return <WardsPage toast={toastActions} />;
      case 'billing':
        return <BillingPage toast={toastActions} />;
      case 'audit':
        return <AuditPage toast={toastActions} />;
      default:
        return <DashboardPage />;
    }
  }

  return (
    <>
      <Layout
        activeTab={activeTab}
        onTabChange={setActiveTab}
        user={auth.user}
        onLogout={auth.logout}
      >
        <Suspense fallback={<PageLoader />}>
          {renderPage()}
        </Suspense>
      </Layout>
      <Toast toasts={toast.toasts} onRemove={toast.removeToast} />
    </>
  );
}
