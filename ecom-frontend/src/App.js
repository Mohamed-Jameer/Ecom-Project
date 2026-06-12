import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

// Components
import Header from './components/Header';
import Footer from './components/Footer';
import Login from './components/Login';
import Registration from './components/Registration';
import Dashboard from './components/Dashboard';
import AdminDashboard from './components/AdminDashboard';
import OAuth2RedirectHandler from './components/OAuth2RedirectHandler';
import Home from './components/Home';
import ProductList from './components/ProductList';
import ProductDetail from './components/ProductDetail';
import Cart from './components/Cart';
import CheckoutPage from './components/CheckoutPage';
import OrderConfirmationPage from './components/OrderConfirmationPage';
import MyOrders from './components/MyOrders';
import ProtectedRoute from './components/ProtectedRoute';
import AccessDenied from './components/AccessDenied';

const App = () => {
  return (
    <Router>
      <div style={appContainerStyle}>
        <Header />
        <main style={mainContentStyle}>
          <Routes>
            {/* Public routes */}
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Registration />} />
            <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
            <Route path="/products" element={<ProductList />} />
            <Route path="/products/:id" element={<ProductDetail />} />
            <Route path="/cart" element={<Cart />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/order-confirmation" element={<OrderConfirmationPage />} />
            <Route path="/my-orders" element={<MyOrders />} />

            {/* User Dashboard */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute allowedRoles={['USER']}>
                  <Dashboard />
                </ProtectedRoute>
              }
            />

            {/* Admin Dashboard */}
            <Route
              path="/admindashboard"
              element={
                <ProtectedRoute allowedRoles={['ADMIN']}>
                  <AdminDashboard />
                </ProtectedRoute>
              }
            />

            {/* Access Denied */}
            <Route path="/access-denied" element={<AccessDenied />} />

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </Router>
  );
};

// ----- Styles -----
const appContainerStyle = {
  display: 'flex',
  flexDirection: 'column',
  minHeight: '100vh',
};

const mainContentStyle = {
  flex: '1',
  padding: '20px',
};

export default App;
