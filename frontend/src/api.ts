import { ApiResponse } from './types';

const API_BASE = '/api/v1';
const TIMEOUT_MS = 15000;

function getAuthHeaders(): Record<string, string> {
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  };
  const token = localStorage.getItem('medicloud_token');
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  const tenantId = localStorage.getItem('medicloud_tenant_id');
  if (tenantId) {
    headers['X-Tenant-ID'] = tenantId;
  }
  return headers;
}

async function request<T>(
  method: string,
  path: string,
  body?: unknown,
  retries = 1
): Promise<ApiResponse<T>> {
  const url = `${API_BASE}${path}`;
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), TIMEOUT_MS);

  for (let attempt = 0; attempt <= retries; attempt++) {
    try {
      const res = await fetch(url, {
        method,
        headers: getAuthHeaders(),
        body: body ? JSON.stringify(body) : undefined,
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (res.status === 401) {
        localStorage.removeItem('medicloud_token');
        localStorage.removeItem('medicloud_user');
        localStorage.removeItem('medicloud_tenant_id');
        window.dispatchEvent(new Event('auth:expired'));
        throw new Error('Session expired. Please log in again.');
      }

      if (res.status === 403) {
        throw new Error('You do not have permission to perform this action.');
      }

      const json: ApiResponse<T> = await res.json();

      if (!res.ok) {
        throw new Error(json.message || `Request failed with status ${res.status}`);
      }

      return json;
    } catch (err: any) {
      clearTimeout(timeoutId);
      if (err.name === 'AbortError') {
        throw new Error('Request timed out. Please try again.');
      }
      if (attempt < retries && !err.message?.includes('Session expired')) {
        await new Promise(r => setTimeout(r, 1000 * (attempt + 1)));
        continue;
      }
      throw err;
    }
  }

  throw new Error('Request failed after retries');
}

export const api = {
  get: <T>(path: string) => request<T>('GET', path),
  post: <T>(path: string, body: unknown) => request<T>('POST', path, body),
  put: <T>(path: string, body: unknown) => request<T>('PUT', path, body),
  delete: <T>(path: string) => request<T>('DELETE', path),
};

// --- Auth helpers ---
export function setAuthData(token: string, user: any, tenantId: string) {
  localStorage.setItem('medicloud_token', token);
  localStorage.setItem('medicloud_user', JSON.stringify(user));
  localStorage.setItem('medicloud_tenant_id', tenantId);
}

export function clearAuthData() {
  localStorage.removeItem('medicloud_token');
  localStorage.removeItem('medicloud_user');
  localStorage.removeItem('medicloud_tenant_id');
}

export function getStoredUser() {
  try {
    const raw = localStorage.getItem('medicloud_user');
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function getStoredToken(): string | null {
  return localStorage.getItem('medicloud_token');
}

export function getStoredTenantId(): string | null {
  return localStorage.getItem('medicloud_tenant_id');
}
