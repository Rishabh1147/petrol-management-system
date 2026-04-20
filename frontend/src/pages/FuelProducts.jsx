import { useCallback, useEffect, useState } from 'react';
import { api } from '../api.js';

function formatMoney(n) {
  return new Intl.NumberFormat(undefined, { style: 'currency', currency: 'INR' }).format(Number(n));
}

export default function FuelProducts() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState(null);
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [editName, setEditName] = useState('');
  const [editPrice, setEditPrice] = useState('');

  const load = useCallback(() => {
    setError(null);
    return api
      .getFuelProducts()
      .then(setItems)
      .catch((e) => setError(e.message));
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  async function onCreate(e) {
    e.preventDefault();
    setError(null);
    try {
      await api.createFuelProduct({ name, pricePerLiter: Number(price) });
      setName('');
      setPrice('');
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  function startEdit(p) {
    setEditingId(p.id);
    setEditName(p.name);
    setEditPrice(String(p.pricePerLiter));
  }

  async function saveEdit() {
    setError(null);
    try {
      await api.updateFuelProduct(editingId, { name: editName, pricePerLiter: Number(editPrice) });
      setEditingId(null);
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  async function remove(id) {
    if (!window.confirm('Delete this fuel product? Tanks referencing it may prevent deletion.')) return;
    setError(null);
    try {
      await api.deleteFuelProduct(id);
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <>
      <h1 className="page-title">Fuel products</h1>
      <p className="page-sub">Manage fuel types and price per liter.</p>
      {error && <div className="error-banner">{error}</div>}

      <div className="panel">
        <h2>Add product</h2>
        <form onSubmit={onCreate}>
          <div className="form-row cols-2">
            <div>
              <label htmlFor="name">Name</label>
              <input
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="e.g. Premium Petrol"
                required
              />
            </div>
            <div>
              <label htmlFor="price">Price per liter (INR)</label>
              <input
                id="price"
                type="number"
                step="0.01"
                min="0.01"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                required
              />
            </div>
          </div>
          <button type="submit" className="btn btn-primary">
            Add product
          </button>
        </form>
      </div>

      <div className="panel">
        <h2>All products</h2>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Price / L</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {items.length === 0 && (
                <tr>
                  <td colSpan={3}>
                    <p className="empty-hint">No products yet.</p>
                  </td>
                </tr>
              )}
              {items.map((p) => (
                <tr key={p.id}>
                  {editingId === p.id ? (
                    <>
                      <td>
                        <input value={editName} onChange={(e) => setEditName(e.target.value)} required />
                      </td>
                      <td>
                        <input
                          type="number"
                          step="0.01"
                          min="0.01"
                          value={editPrice}
                          onChange={(e) => setEditPrice(e.target.value)}
                          required
                        />
                      </td>
                      <td className="actions-inline">
                        <button type="button" className="btn btn-primary" onClick={() => saveEdit()}>
                          Save
                        </button>
                        <button type="button" className="btn btn-ghost" onClick={() => setEditingId(null)}>
                          Cancel
                        </button>
                      </td>
                    </>
                  ) : (
                    <>
                      <td>{p.name}</td>
                      <td>{formatMoney(p.pricePerLiter)}</td>
                      <td className="actions-inline">
                        <button type="button" className="btn btn-ghost" onClick={() => startEdit(p)}>
                          Edit
                        </button>
                        <button type="button" className="btn btn-danger" onClick={() => remove(p.id)}>
                          Delete
                        </button>
                      </td>
                    </>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}
