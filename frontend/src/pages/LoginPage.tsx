import React, { useState } from 'react';

interface LoginPageProps {
  onLogin: (email: string, password: string) => Promise<{ success: boolean; error?: string }>;
}

export default function LoginPage({ onLogin }: LoginPageProps) {
  const [email, setEmail] = useState('dr.vishnu@ayushhealth.com');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    if (!email || !password) {
      setError('Please enter both email and password.');
      return;
    }
    
    setIsLoading(true);
    setError(null);
    try {
      const result = await onLogin(email, password);
      if (!result.success) {
        setError(result.error || 'Login failed. Please check your credentials.');
      }
    } catch (err) {
      setError('An unexpected error occurred during login.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSubmit();
    }
  };

  return (
    <div className="flex flex-col flex-center animate-fade-in" style={{ minHeight: '100vh', backgroundColor: 'var(--bg-dark, #0a0f1a)' }}>
      <div 
        className="card flex flex-col gap-6" 
        style={{ 
          maxWidth: '420px', 
          width: '100%', 
          borderTop: '3px solid var(--accent, #14b8a6)',
          margin: '0 auto' 
        }}
      >
        <div className="flex flex-col flex-center gap-4 text-center">
          <div style={{ color: 'var(--accent, #14b8a6)' }}>
            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M12 2v20M2 12h20" />
            </svg>
          </div>
          <div>
            <h1 className="page-title" style={{ margin: 0, fontSize: '24px' }}>MediCloud OS</h1>
            <p className="page-subtitle" style={{ margin: '4px 0 0 0', opacity: 0.7, fontSize: '14px' }}>Ayush Health Network</p>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4 w-full">
          {error && (
            <div className="badge badge-info" style={{ backgroundColor: 'rgba(239, 68, 68, 0.1)', color: '#ef4444', border: '1px solid #ef4444' }}>
              {error}
            </div>
          )}

          <div className="form-group flex flex-col gap-2">
            <label htmlFor="email" className="label">Email Address</label>
            <input
              id="email"
              type="email"
              className="input w-full"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              onKeyDown={handleKeyDown}
              autoComplete="username"
              placeholder="dr.name@ayushhealth.com"
              disabled={isLoading}
            />
          </div>

          <div className="form-group flex flex-col gap-2">
            <label htmlFor="password" className="label">Password</label>
            <input
              id="password"
              type="password"
              className="input w-full"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyDown={handleKeyDown}
              autoComplete="current-password"
              placeholder="••••••••"
              disabled={isLoading}
            />
          </div>

          <button 
            type="submit" 
            className="btn btn-primary w-full flex flex-center gap-2" 
            disabled={isLoading}
            style={{ marginTop: '8px' }}
          >
            {isLoading ? (
              <>
                <span className="spinner" style={{ display: 'inline-block', width: '16px', height: '16px', border: '2px solid rgba(255,255,255,0.3)', borderTopColor: 'white', borderRadius: '50%', animation: 'spin 1s linear infinite' }}></span>
                Signing in...
              </>
            ) : (
              'Sign In'
            )}
          </button>
        </form>
      </div>
      <style>{`
        @keyframes spin {
          to { transform: rotate(360deg); }
        }
      `}</style>
    </div>
  );
};
