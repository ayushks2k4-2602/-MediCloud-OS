import React, { useState, useEffect } from 'react';
import Sidebar from './Sidebar';

interface HeaderProps {
  onMenuClick: () => void;
  hospitalName: string;
}

function Header({ onMenuClick, hospitalName }: HeaderProps) {
  return (
    <header className="header">
      <div className="header-left">
        <button
          className="btn-ghost btn-icon hamburger-btn"
          onClick={onMenuClick}
          aria-label="Toggle navigation menu"
        >
          ☰
        </button>
        <span className="badge" style={{ background: 'var(--accent-glow)', color: 'var(--accent)', border: '1px solid var(--accent)' }}>
          🏥 {hospitalName}
        </span>
      </div>

      <div className="header-right">
        <span className="badge badge-success">● Connected</span>
        <a
          href="http://localhost:8082/swagger-ui.html"
          target="_blank"
          rel="noreferrer"
          className="btn btn-ghost btn-sm"
        >
          API Docs ↗
        </a>
      </div>
    </header>
  );
}

interface LayoutProps {
  children: React.ReactNode;
  activeTab: string;
  onTabChange: (tab: string) => void;
  user: { fullName: string; role: string } | null;
  onLogout: () => void;
}

export default function Layout({
  children,
  activeTab,
  onTabChange,
  user,
  onLogout,
}: LayoutProps) {
  const [isCollapsed, setIsCollapsed] = useState(false);

  useEffect(() => {
    const checkMobile = () => {
      if (window.innerWidth < 768) {
        setIsCollapsed(true);
      }
    };
    checkMobile();
    window.addEventListener('resize', checkMobile);
    return () => window.removeEventListener('resize', checkMobile);
  }, []);

  const toggleCollapse = () => setIsCollapsed((prev) => !prev);

  return (
    <div className="app-layout">
      <Sidebar
        activeTab={activeTab}
        onTabChange={(tab) => {
          onTabChange(tab);
          if (window.innerWidth < 768) setIsCollapsed(true);
        }}
        isCollapsed={isCollapsed}
        onToggleCollapse={toggleCollapse}
        userName={user?.fullName || 'Guest'}
        userRole={user?.role || 'User'}
        onLogout={onLogout}
      />
      <div className="main-content">
        <Header
          onMenuClick={toggleCollapse}
          hospitalName="Ayush Health Network"
        />
        <main className="content-area animate-fade-in">
          {children}
        </main>
      </div>
    </div>
  );
}
