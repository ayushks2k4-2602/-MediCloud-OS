import React from 'react';
import { EmptyState } from './EmptyState';
import { SkeletonTable } from './LoadingSkeleton';

export interface Column<T> {
  key: string;
  label: string;
  render?: (row: T) => React.ReactNode;
}

export interface DataTableProps<T> {
  columns: Column<T>[];
  data: T[];
  loading?: boolean;
  emptyMessage?: string;
  page?: number;
  totalPages?: number;
  onPageChange?: (page: number) => void;
}

export function DataTable<T extends Record<string, any>>({
  columns,
  data,
  loading,
  emptyMessage = 'No data available',
  page,
  totalPages,
  onPageChange
}: DataTableProps<T>) {
  if (loading) {
    return <SkeletonTable cols={columns.length} rows={5} />;
  }

  if (data.length === 0) {
    return <EmptyState title="No Data" description={emptyMessage} />;
  }

  return (
    <div className="w-full">
      <table className="data-table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.key} scope="col">{col.label}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {columns.map((col) => (
                <td key={col.key}>
                  {col.render ? col.render(row) : row[col.key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      
      {page !== undefined && totalPages !== undefined && onPageChange && (
        <div className="flex flex-between mt-4">
          <button 
            className="btn btn-secondary btn-sm" 
            onClick={() => onPageChange(page - 1)}
            disabled={page <= 1}
          >
            Previous
          </button>
          <span>Page {page} of {totalPages}</span>
          <button 
            className="btn btn-secondary btn-sm" 
            onClick={() => onPageChange(page + 1)}
            disabled={page >= totalPages}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}
