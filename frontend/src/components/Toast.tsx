import React from 'react';

export interface ToastMessage {
  id: string;
  type: 'success' | 'error' | 'info' | 'warning';
  message: string;
}

export interface ToastProps {
  toasts: ToastMessage[];
  onRemove: (id: string) => void;
}

export default function Toast({ toasts, onRemove }: ToastProps) {
  return (
    <div className="toast-container" role="alert" aria-live="assertive">
      {toasts.map((toast) => (
        <div key={toast.id} className={`toast toast-${toast.type} animate-slide-right`}>
          <div className="toast-icon">
            {toast.type === 'success' && '✓'}
            {toast.type === 'error' && '✕'}
            {toast.type === 'info' && 'ℹ'}
            {toast.type === 'warning' && '⚠'}
          </div>
          <div className="toast-message">{toast.message}</div>
          <button className="btn-icon" onClick={() => onRemove(toast.id)} aria-label="Close">
            ✕
          </button>
        </div>
      ))}
    </div>
  );
};
