import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import useCart from '../hooks/useCart';

const Header = () => {
    const navigate = useNavigate();
    const { cartCount } = useCart();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
      const token = localStorage.getItem('jwtToken');
      setIsLoggedIn(!!token);

      const handleLoginLogout = () => {
          setIsLoggedIn(!!localStorage.getItem('jwtToken'));
      };

      window.addEventListener('login', handleLoginLogout);
      window.addEventListener('logout', handleLoginLogout);

      return () => {
          window.removeEventListener('login', handleLoginLogout);
          window.removeEventListener('logout', handleLoginLogout);
      };
  }, []);



    const handleLogout = () => {
        localStorage.removeItem('jwtToken');
        window.dispatchEvent(new Event('logout')); // notify Header
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
                    <li><Link to="/about" style={linkStyle}>About</Link></li>
                    <li><Link to="/contact" style={linkStyle}>Contact</Link></li>

                    {isLoggedIn ? (
                        <>
                            <li><Link to="/dashboard" style={linkStyle}>Dashboard</Link></li>
                            <li><button onClick={handleLogout} style={buttonStyle}>Logout</button></li>
                        </>
                    ) : (
                        <>
                            <li><Link to="/login" style={linkStyle}>Login</Link></li>
                            <li><Link to="/register" style={linkStyle}>Register</Link></li>
                        </>
                    )}

                    <li>
                        <Link to="/cart" style={linkStyle}>
                            <span role="img" aria-label="cart">🛒</span> Cart ({cartCount})
                        </Link>
                    </li>
                </ul>
            </nav>
        </header>
    );
};

const headerStyle = {
    background: '#333',
    color: '#fff',
    padding: '1rem 2rem',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    boxShadow: '0 2px 5px rgba(0, 0, 0, 0.2)',
};

const brandStyle = {
    fontSize: '1.5rem',
    fontWeight: 'bold',
};

const navStyle = {
    flex: '1',
    display: 'flex',
    justifyContent: 'flex-end',
};

const ulStyle = {
    listStyle: 'none',
    margin: '0',
    padding: '0',
    display: 'flex',
    alignItems: 'center',
    gap: '20px',
};

const linkStyle = {
    color: '#fff',
    textDecoration: 'none',
    fontWeight: 'bold',
    transition: 'color 0.3s ease',
};

const buttonStyle = {
    background: 'none',
    border: 'none',
    color: '#fff',
    cursor: 'pointer',
    fontWeight: 'bold',
    fontSize: '1rem',
    padding: '0',
};

export default Header;
