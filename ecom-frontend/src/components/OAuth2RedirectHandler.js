import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // Parse query params from Google redirect
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
    const role = params.get('role');

    if (token && role) {
      // Store token and role (uppercase to match allowedRoles)
      localStorage.setItem('jwtToken', token);
      localStorage.setItem('role', role.toUpperCase());

      // Trigger login event for Header
      window.dispatchEvent(new Event('login'));

      // Redirect based on role
      if (role.toUpperCase() === 'ADMIN') navigate('/admindashboard', { replace: true });
      else navigate('/dashboard', { replace: true });
    } else {
      navigate('/login', { replace: true });
    }
  }, [navigate]);

  return <div style={{ textAlign: 'center', marginTop: '50px' }}>Redirecting...</div>;
};

export default OAuth2RedirectHandler;
