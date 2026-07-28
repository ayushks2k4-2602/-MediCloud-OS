import React from 'react';

export interface BadgeProps {
  label: string;
  variant: 'success' | 'danger' | 'warning' | 'info' | 'neutral';
  size?: 'sm' | 'md';
}

export const Badge: React.FC<BadgeProps> = ({ label, variant, size = 'md' }) => {
  return (
    <span className={`badge badge-${variant} ${size === 'sm' ? 'text-xs px-2' : ''}`}>
      {label}
    </span>
  );
};
