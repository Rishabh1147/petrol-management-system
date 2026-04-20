import { NavLink } from 'react-router-dom';

export default function Layout({ children }) {
  return (
    <div className="app-shell">
      <header className="top-nav">
        <div className="brand">
          <span className="brand-mark" aria-hidden>
            ⛽
          </span>
          <span>Petrol Management</span>
        </div>
        <nav className="nav-links">
          <NavLink to="/" end>
            Dashboard
          </NavLink>
          <NavLink to="/products">Fuel products</NavLink>
          <NavLink to="/tanks">Tanks</NavLink>
          <NavLink to="/sales">Sales</NavLink>
        </nav>
      </header>
      <main className="page">{children}</main>
    </div>
  );
}
