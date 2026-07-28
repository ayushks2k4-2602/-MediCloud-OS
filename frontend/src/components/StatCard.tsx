import React from 'react';

export interface StatCardProps {
  label: string;
  value: string | number;
  trend?: string;
  trendDirection?: 'up' | 'down' | 'neutral';
  icon?: string;
}

export const StatCard: React.FC<StatCardProps> = ({ label, value, trend, trendDirection, icon }) => {
  return (
    <div className="stat-card card p-4">
      <div className="flex flex-between">
        <h4 className="stat-label text-sm" style={{ color: 'var(--text-muted)' }}>{label}</h4>
        {icon && <div className="stat-icon" style={{ color: 'var(--color-primary)' }}>{icon}</div>}
      </div>
      <div className="mt-2 flex gap-2" style={{ alignItems: 'baseline' }}>
        <div className="stat-value" style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{value}</div>
        {trend && (
          <span className={`stat-trend text-sm ${
            trendDirection === 'up' ? 'text-success' : 
            trendDirection === 'down' ? 'text-danger' : 'text-neutral'
          }`}>
            {trendDirection === 'up' && '↑ '}
            {trendDirection === 'down' && '↓ '}
            {trend}
          </span>
        )}
      </div>
    </div>
  );
};
