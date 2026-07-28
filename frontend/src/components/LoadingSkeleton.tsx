import React from 'react';

export const SkeletonTable: React.FC<{ rows?: number, cols?: number }> = ({ rows = 5, cols = 4 }) => {
  return (
    <div className="skeleton">
      <table className="data-table">
        <thead>
          <tr>
            {Array.from({ length: cols }).map((_, i) => (
              <th key={i}><div className="skeleton-text" /></th>
            ))}
          </tr>
        </thead>
        <tbody>
          {Array.from({ length: rows }).map((_, i) => (
            <tr key={i}>
              {Array.from({ length: cols }).map((_, j) => (
                <td key={j}><div className="skeleton-row" /></td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export const SkeletonCards: React.FC<{ count?: number }> = ({ count = 3 }) => {
  return (
    <div className="flex gap-4 flex-col">
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} className="skeleton-card" />
      ))}
    </div>
  );
};

export const SkeletonForm: React.FC = () => {
  return (
    <div className="flex flex-col gap-4 skeleton">
      <div className="skeleton-text" style={{ width: '30%' }} />
      <div className="skeleton-row" />
      <div className="skeleton-text" style={{ width: '40%' }} />
      <div className="skeleton-row" />
      <div className="skeleton-row" style={{ height: '100px' }} />
    </div>
  );
};
