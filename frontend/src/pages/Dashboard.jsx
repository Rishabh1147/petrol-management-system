import { useEffect, useState } from 'react';
import { api } from '../api.js';

function formatMoney(n) {
  if (n == null) return '—';
  return new Intl.NumberFormat(undefined, { style: 'currency', currency: 'INR' }).format(Number(n));
}

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    let cancelled = false;
    api
      .getStats()
      .then((s) => {
        if (!cancelled) setStats(s);
      })
      .catch((e) => {
        if (!cancelled) setError(e.message);
      });
    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <>
      <h1 className="page-title">Dashboard</h1>
      <p className="page-sub">Today&apos;s revenue and station snapshot.</p>
      {error && <div className="error-banner">{error}</div>}
      <div className="grid-stats">
        <div className="stat-card">
          <div className="stat-label">Sales today</div>
          <div className="stat-value">{stats ? formatMoney(stats.totalSalesToday) : '…'}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Transactions today</div>
          <div className="stat-value">{stats != null ? stats.saleCountToday : '…'}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Tanks</div>
          <div className="stat-value">{stats != null ? stats.tankCount : '…'}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Fuel products</div>
          <div className="stat-value">{stats != null ? stats.productCount : '…'}</div>
        </div>
      </div>
      <div className="panel">
        <h2>Getting started</h2>
        <p className="empty-hint">
          Use <strong>Fuel products</strong> to set prices, <strong>Tanks</strong> to track storage levels, and{' '}
          <strong>Sales</strong> to dispense fuel and record transactions. The API runs on port 8080; start the UI
          with <code>npm run dev</code> in <code>frontend/</code>.
        </p>
      </div>
    </>
  );
}
