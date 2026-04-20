import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout.jsx';
import Dashboard from './pages/Dashboard.jsx';
import FuelProducts from './pages/FuelProducts.jsx';
import Tanks from './pages/Tanks.jsx';
import Sales from './pages/Sales.jsx';
import './App.css';

export default function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/products" element={<FuelProducts />} />
        <Route path="/tanks" element={<Tanks />} />
        <Route path="/sales" element={<Sales />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Layout>
  );
}
