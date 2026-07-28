import React from 'react';
import { NAV_ITEMS } from '../constants';
import { getInitials } from '../utils';

interface SidebarProps {
  activeTab: string;
  onTabChange: (tab: string) => void;
  isCollapsed: boolean;
  onToggleCollapse: () => void;
  userName: string;
  userRole: string;
  onLogout: () => void;
}

export default function Sidebar({
  activeTab,
  onTabChange,
  isCollapsed,
  onToggleCollapse,
  userName,
  userRole,
  onLogout,
}: SidebarProps) {
  return (
    <>
      {/* Mobile overlay */}
      {!isCollapsed && (
        <div
          className="sidebar-overlay"
          onClick={onToggleCollapse}
          aria-hidden="true"
        />
      )}

      <aside className={`sidebar ${isCollapsed ? 'collapsed' : ''}`}>
        {/* Logo & Collapse Toggle */}
        <div className="sidebar-top">
          <div className="sidebar-logo">
            <div className="logo-icon" aria-hidden="true">✚</div>
            {!isCollapsed && (
              <div className="logo-text">
                <h2 className="logo-title">MediCloud OS</h2>
                <span className="logo-subtitle">Ayush Health Network</span>
              </div>
            )}
          </div>
          <button
            className="btn-ghost btn-icon collapse-toggle"
            onClick={onToggleCollapse}
            aria-label={isCollapsed ? 'Expand sidebar' : 'Collapse sidebar'}
            title={isCollapsed ? 'Expand' : 'Collapse'}
          >
            {isCollapsed ? '▸' : '◂'}
          </button>
        </div>

        {/* Navigation */}
        <nav className="sidebar-nav" aria-label="Main navigation">
          {NAV_ITEMS.map((item) => (
            <button
              key={item.id}
              className={`nav-item ${activeTab === item.id ? 'active' : ''}`}
              onClick={() => onTabChange(item.id)}
              aria-current={activeTab === item.id ? 'page' : undefined}
              title={isCollapsed ? item.label : undefined}
            >
              <span className="nav-icon" aria-hidden="true">{item.icon}</span>
              {!isCollapsed && <span className="nav-label">{item.label}</span>}
            </button>
          ))}
        </nav>

        {/* User Section */}
        <div className="sidebar-footer">
          <div className="sidebar-user">
            <div className="user-avatar" aria-hidden="true">
              {getInitials(userName)}
            </div>
            {!isCollapsed && (
              <div className="user-info">
                <p className="user-name">{userName}</p>
                <p className="user-role">{userRole}</p>
              </div>
            )}
          </div>
          <button
            className="btn-ghost btn-icon"
            onClick={onLogout}
            aria-label="Sign out"
            title="Sign out"
          >
            ⏻
          </button>
        </div>
      </aside>
    </>
  );
}
