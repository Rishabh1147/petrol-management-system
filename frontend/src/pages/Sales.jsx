import { useCallback, useEffect, useState } from 'react';
import { api } from '../api.js';

function formatMoney(n) {
  return new Intl.NumberFormat(undefined, { style: 'currency', currency: 'INR' }).format(Number(n));
}

function formatTime(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString();
}

export default function Sales() {
  const [sales, setSales] = useState([]);
  const [products, setProducts] = useState([]);
  const [tanks, setTanks] = useState([]);
  const [error, setError] = useState(null);
  const [fuelProductId, setFuelProductId] = useState('');
  const [tankId, setTankId] = useState('');
  const [liters, setLiters] = useState('');

  const load = useCallback(() => {
    setError(null);
    return Promise.all([api.getSales(), api.getFuelProducts(), api.getTanks()])
      .then(([s, p, t]) => {
        setSales(s);
        setProducts(p);
        setTanks(t);
      })
      .catch((e) => setError(e.message));
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const tanksForProduct = tanks.filter((x) => String(x.fuelProductId) === String(fuelProductId));

  useEffect(() => {
    if (tanksForProduct.length && !tanksForProduct.some((x) => String(x.id) === String(tankId))) {
      setTankId(String(tanksForProduct[0].id));
    }
  }, [fuelProductId, tanks, tankId, tanksForProduct]);

  useEffect(() => {
    if (products.length && !fuelProductId) {
      setFuelProductId(String(products[0].id));
    }
  }, [products, fuelProductId]);

  async function onSale(e) {
    e.preventDefault();
    setError(null);
    try {
      await api.createSale({
        fuelProductId: Number(fuelProductId),
        tankId: Number(tankId),
        liters: Number(liters),
      });
      setLiters('');
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <>
      <h1 className="page-title">Sales</h1>
      <p className="page-sub">Record a dispense: price comes from the fuel product; stock is reduced on the tank.</p>
      {error && <div className="error-banner">{error}</div>}

      <div className="panel">
        <h2>New sale</h2>
        <form onSubmit={onSale}>
          <div className="form-row cols-2">
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
                    {p.name} ({formatMoney(p.pricePerLiter)}/L)
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label htmlFor="tk">Tank</label>
              <select id="tk" value={tankId} onChange={(e) => setTankId(e.target.value)} required>
                {tanksForProduct.length === 0 && <option value="">No tank for this product</option>}
                {tanksForProduct.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.label} ({Number(t.currentLiters).toLocaleString()} L)
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label htmlFor="L">Liters</label>
              <input
                id="L"
                type="number"
                step="0.01"
                min="0.01"
                value={liters}
                onChange={(e) => setLiters(e.target.value)}
                required
              />
            </div>
          </div>
          <button type="submit" className="btn btn-primary" disabled={!products.length || !tanksForProduct.length}>
            Record sale
          </button>
        </form>
      </div>

      <div className="panel">
        <h2>Recent sales</h2>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>When</th>
                <th>Product</th>
                <th>Tank</th>
                <th>Liters</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              {sales.length === 0 && (
                <tr>
                  <td colSpan={5}>
                    <p className="empty-hint">No sales recorded yet.</p>
                  </td>
                </tr>
              )}
              {sales.map((s) => (
                <tr key={s.id}>
                  <td>{formatTime(s.soldAt)}</td>
                  <td>{s.fuelProductName}</td>
                  <td>{s.tankLabel ?? '—'}</td>
                  <td>{Number(s.liters).toLocaleString()}</td>
                  <td>{formatMoney(s.totalAmount)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}
