import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import useAuth from '../hooks/useAuth';
import useCart from '../hooks/useCart';

const Header = () => {
  const navigate = useNavigate();
  const { cartCount } = useCart();
  const { isLoggedIn, role } = useAuth();

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('role');
    window.dispatchEvent(new Event('logout'));
    navigate('/login', { replace: true });
  };

  return (
    <header style={headerStyle}>
      <div style={brandStyle}>
        <Link to="/" style={linkStyle}>E-Commerce App</Link>
      </div>
      <nav style={navStyle}>
        <ul style={ulStyle}>
          <li><Link to="/" style={linkStyle}>Home</Link></li>
          <li><Link to="/products" style={linkStyle}>Products</Link></li>
          {isLoggedIn ? (
            <>
              {role === 'ADMIN' ? (
                <li><Link to="/admindashboard" style={linkStyle}>Admin Dashboard</Link></li>
              ) : (
                <li><Link to="/dashboard" style={linkStyle}>Dashboard</Link></li>
              )}
              <li><button onClick={handleLogout} style={buttonStyle}>Logout</button></li>
            </>
          ) : (
            <>
              <li><Link to="/login" style={linkStyle}>Login</Link></li>
              <li><Link to="/register" style={linkStyle}>Register</Link></li>
            </>
          )}
          <li><Link to="/cart" style={linkStyle}>🛒 Cart ({cartCount})</Link></li>
        </ul>
      </nav>
    </header>
  );
};

// Styles same as before
const headerStyle = { background: '#333', color: '#fff', padding: '1rem 2rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' };
const brandStyle = { fontSize: '1.5rem', fontWeight: 'bold' };
const navStyle = { flex: '1', display: 'flex', justifyContent: 'flex-end' };
const ulStyle = { listStyle: 'none', margin: 0, padding: 0, display: 'flex', alignItems: 'center', gap: '20px' };
const linkStyle = { color: '#fff', textDecoration: 'none', fontWeight: 'bold' };
const buttonStyle = { background: 'none', border: 'none', color: '#fff', cursor: 'pointer', fontWeight: 'bold' };

export default Header;
