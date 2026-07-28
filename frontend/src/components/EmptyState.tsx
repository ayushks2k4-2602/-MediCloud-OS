import React from 'react';

export interface EmptyStateProps {
  icon?: string;
  title: string;
  description?: string;
  action?: {
    label: string;
    onClick: () => void;
  };
}

export const EmptyState: React.FC<EmptyStateProps> = ({ icon, title, description, action }) => {
  return (
    <div className="empty-state card flex flex-col flex-center text-center p-6 gap-4">
      {icon && <div style={{ fontSize: '2rem' }}>{icon}</div>}
      <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>{title}</h3>
      {description && <p style={{ color: 'var(--text-muted)' }}>{description}</p>}
      {action && (
        <button className="btn btn-primary mt-2" onClick={action.onClick}>
          {action.label}
        </button>
      )}
    </div>
  );
};
