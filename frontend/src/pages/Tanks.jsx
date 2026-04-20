import { useCallback, useEffect, useState } from 'react';
import { api } from '../api.js';

export default function Tanks() {
  const [tanks, setTanks] = useState([]);
  const [products, setProducts] = useState([]);
  const [error, setError] = useState(null);
  const [label, setLabel] = useState('');
  const [fuelProductId, setFuelProductId] = useState('');
  const [capacity, setCapacity] = useState('');
  const [current, setCurrent] = useState('');
  const [levelEdits, setLevelEdits] = useState({});

  const load = useCallback(() => {
    setError(null);
    return Promise.all([api.getTanks(), api.getFuelProducts()])
      .then(([t, p]) => {
        setTanks(t);
        setProducts(p);
      })
      .catch((e) => setError(e.message));
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  useEffect(() => {
    if (products.length && fuelProductId === '') {
      setFuelProductId(String(products[0].id));
    }
  }, [products, fuelProductId]);

  async function onCreate(e) {
    e.preventDefault();
    setError(null);
    try {
      await api.createTank({
        label,
        fuelProductId: Number(fuelProductId),
        capacityLiters: Number(capacity),
        currentLiters: Number(current),
      });
      setLabel('');
      setCapacity('');
      setCurrent('');
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  async function applyLevel(id) {
    const val = levelEdits[id];
    if (val == null || val === '') return;
    setError(null);
    try {
      await api.patchTankLevel(id, Number(val));
      setLevelEdits((prev) => {
        const next = { ...prev };
        delete next[id];
        return next;
      });
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  async function remove(id) {
    if (!window.confirm('Delete this tank?')) return;
    setError(null);
    try {
      await api.deleteTank(id);
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  function pct(t) {
    const cap = Number(t.capacityLiters);
    const cur = Number(t.currentLiters);
    if (!cap) return 0;
    return Math.min(100, Math.round((cur / cap) * 1000) / 10);
  }

  return (
    <>
      <h1 className="page-title">Tanks</h1>
      <p className="page-sub">Storage capacity, current level, and manual level adjustments after delivery.</p>
      {error && <div className="error-banner">{error}</div>}

      <div className="panel">
        <h2>Add tank</h2>
        <form onSubmit={onCreate}>
          <div className="form-row cols-2">
            <div>
              <label htmlFor="label">Label</label>
              <input id="label" value={label} onChange={(e) => setLabel(e.target.value)} required />
            </div>
            <div>
              <label htmlFor="fp">Fuel product</label>
              <select
                id="fp"
                value={fuelProductId}
                onChange={(e) => setFuelProductId(e.target.value)}
                required
              >
                {products.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label htmlFor="cap">Capacity (L)</label>
              <input
                id="cap"
                type="number"
                step="0.01"
                min="0.01"
                value={capacity}
                onChange={(e) => setCapacity(e.target.value)}
                required
              />
            </div>
            <div>
              <label htmlFor="cur">Current level (L)</label>
              <input
                id="cur"
                type="number"
                step="0.01"
                min="0"
                value={current}
                onChange={(e) => setCurrent(e.target.value)}
                required
              />
            </div>
          </div>
          <button type="submit" className="btn btn-primary" disabled={!products.length}>
            Add tank
          </button>
        </form>
      </div>

      <div className="panel">
        <h2>All tanks</h2>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Label</th>
                <th>Fuel</th>
                <th>Level</th>
                <th>Adjust</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tanks.length === 0 && (
                <tr>
                  <td colSpan={5}>
                    <p className="empty-hint">No tanks yet. Add a fuel product first.</p>
                  </td>
                </tr>
              )}
              {tanks.map((t) => (
                <tr key={t.id}>
                  <td>{t.label}</td>
                  <td>
                    <span className="badge">{t.fuelProductName}</span>
                  </td>
                  <td>
                    <div>
                      {Number(t.currentLiters).toLocaleString()} / {Number(t.capacityLiters).toLocaleString()} L
                    </div>
                    <div className="meter" title={`${pct(t)}%`}>
                      <div className="meter-fill" style={{ width: `${pct(t)}%` }} />
                    </div>
                  </td>
                  <td>
                    <div className="actions-inline">
                      <input
                        type="number"
                        step="0.01"
                        min="0"
                        placeholder="New level"
                        style={{ maxWidth: '120px' }}
                        value={levelEdits[t.id] ?? ''}
                        onChange={(e) => setLevelEdits((prev) => ({ ...prev, [t.id]: e.target.value }))}
                      />
                      <button type="button" className="btn btn-ghost" onClick={() => applyLevel(t.id)}>
                        Set level
                      </button>
                    </div>
                  </td>
                  <td>
                    <button type="button" className="btn btn-danger" onClick={() => remove(t.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}
