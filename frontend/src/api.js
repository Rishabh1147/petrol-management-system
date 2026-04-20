const API_BASE = import.meta.env.VITE_API_BASE || '';

async function handle(res) {
  if (!res.ok) {
    let detail = res.statusText;
    try {
      const body = await res.json();
      detail = body.detail || body.message || JSON.stringify(body);
    } catch {
      /* ignore */
    }
    throw new Error(detail || `Request failed: ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export const api = {
  getStats: () => fetch(`${API_BASE}/api/dashboard/stats`).then(handle),

  getFuelProducts: () => fetch(`${API_BASE}/api/fuel-products`).then(handle),
  createFuelProduct: (body) =>
    fetch(`${API_BASE}/api/fuel-products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    }).then(handle),
  updateFuelProduct: (id, body) =>
    fetch(`${API_BASE}/api/fuel-products/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    }).then(handle),
  deleteFuelProduct: (id) =>
    fetch(`${API_BASE}/api/fuel-products/${id}`, { method: 'DELETE' }).then(handle),

  getTanks: () => fetch(`${API_BASE}/api/tanks`).then(handle),
  createTank: (body) =>
    fetch(`${API_BASE}/api/tanks`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    }).then(handle),
  patchTankLevel: (id, currentLiters) =>
    fetch(`${API_BASE}/api/tanks/${id}/level`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ currentLiters }),
    }).then(handle),
  deleteTank: (id) => fetch(`${API_BASE}/api/tanks/${id}`, { method: 'DELETE' }).then(handle),

  getSales: () => fetch(`${API_BASE}/api/sales`).then(handle),
  createSale: (body) =>
    fetch(`${API_BASE}/api/sales`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    }).then(handle),
};
