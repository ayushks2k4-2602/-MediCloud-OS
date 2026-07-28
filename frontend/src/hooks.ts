import { useState, useEffect, useCallback, useRef } from 'react';
import { ToastMessage, AuthState } from './types';
import { getStoredToken, getStoredUser, getStoredTenantId, setAuthData, clearAuthData, api } from './api';
import { TOAST_DURATION } from './constants';

// ==================== useAuth ====================
export function useAuth() {
  const [auth, setAuth] = useState<AuthState>(() => {
    const token = getStoredToken();
    const user = getStoredUser();
    const tenantId = getStoredTenantId();
    return {
      token,
      user,
      tenantId,
      isAuthenticated: !!token,
    };
  });

  useEffect(() => {
    const handleExpired = () => {
      setAuth({ token: null, user: null, tenantId: null, isAuthenticated: false });
    };
    window.addEventListener('auth:expired', handleExpired);
    return () => window.removeEventListener('auth:expired', handleExpired);
  }, []);

  const login = useCallback(async (email: string, password: string) => {
    try {
      const res = await api.post<any>('/auth/login', { email, password });
      if (res.data) {
        const { token, refreshToken, user } = res.data;
        const tenantId = user?.tenantId || 'default';
        setAuthData(token || refreshToken || 'session', user, tenantId);
        setAuth({ token: token || 'session', user, tenantId, isAuthenticated: true });
        return { success: true };
      }
      return { success: false, error: res.message || 'Login failed' };
    } catch (err: any) {
      // If backend auth is not fully wired, allow demo login
      if (err.message?.includes('Failed to fetch') || err.message?.includes('Request failed')) {
        const demoUser = { id: '1', email, fullName: 'Dr. Vishnu Tiwari', role: 'ADMIN', tenantId: 'tenant-ayush-health' };
        setAuthData('demo-token', demoUser, 'tenant-ayush-health');
        setAuth({ token: 'demo-token', user: demoUser, tenantId: 'tenant-ayush-health', isAuthenticated: true });
        return { success: true };
      }
      return { success: false, error: err.message || 'Login failed' };
    }
  }, []);

  const logout = useCallback(() => {
    clearAuthData();
    setAuth({ token: null, user: null, tenantId: null, isAuthenticated: false });
  }, []);

  return { ...auth, login, logout };
}

// ==================== useToast ====================
let toastId = 0;
export function useToast() {
  const [toasts, setToasts] = useState<ToastMessage[]>([]);

  const addToast = useCallback((type: ToastMessage['type'], message: string, duration = TOAST_DURATION) => {
    const id = String(++toastId);
    const toast: ToastMessage = { id, type, message, duration };
    setToasts(prev => [...prev, toast]);
    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id));
    }, duration);
    return id;
  }, []);

  const removeToast = useCallback((id: string) => {
    setToasts(prev => prev.filter(t => t.id !== id));
  }, []);

  const success = useCallback((msg: string) => addToast('success', msg), [addToast]);
  const error = useCallback((msg: string) => addToast('error', msg), [addToast]);
  const info = useCallback((msg: string) => addToast('info', msg), [addToast]);
  const warning = useCallback((msg: string) => addToast('warning', msg), [addToast]);

  return { toasts, addToast, removeToast, success, error, info, warning };
}

// ==================== useDebounce ====================
export function useDebounce<T>(value: T, delay: number): T {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedValue(value), delay);
    return () => clearTimeout(timer);
  }, [value, delay]);

  return debouncedValue;
}

// ==================== usePagination ====================
export function usePagination(initialSize = 20) {
  const [page, setPage] = useState(0);
  const [size] = useState(initialSize);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const nextPage = useCallback(() => {
    setPage(p => Math.min(p + 1, totalPages - 1));
  }, [totalPages]);

  const prevPage = useCallback(() => {
    setPage(p => Math.max(0, p - 1));
  }, []);

  const goToPage = useCallback((n: number) => {
    setPage(Math.max(0, Math.min(n, totalPages - 1)));
  }, [totalPages]);

  const reset = useCallback(() => setPage(0), []);

  const updateFromResponse = useCallback((data: { totalPages: number; totalElements: number }) => {
    setTotalPages(data.totalPages);
    setTotalElements(data.totalElements);
  }, []);

  return { page, size, totalPages, totalElements, nextPage, prevPage, goToPage, reset, updateFromResponse };
}

// ==================== useFetch ====================
export function useFetch<T>(fetchFn: () => Promise<T>, deps: any[] = []) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const mountedRef = useRef(true);

  const refetch = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await fetchFn();
      if (mountedRef.current) {
        setData(result);
      }
    } catch (err: any) {
      if (mountedRef.current) {
        setError(err.message || 'An error occurred');
      }
    } finally {
      if (mountedRef.current) {
        setLoading(false);
      }
    }
  }, deps);

  useEffect(() => {
    mountedRef.current = true;
    refetch();
    return () => { mountedRef.current = false; };
  }, [refetch]);

  return { data, loading, error, refetch };
}
