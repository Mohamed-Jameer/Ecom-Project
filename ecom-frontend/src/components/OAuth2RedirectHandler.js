import React, { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const OAuth2RedirectHandler = () => {
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const token = queryParams.get('token');

        if (token) {
            localStorage.setItem('jwtToken', token);
            // Correct: Redirect to a frontend route
            navigate('/dashboard', { replace: true });
        } else {
            navigate('/login', { replace: true });
        }
    }, [navigate, location.search]);

    return (
        <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <h2>Processing OAuth2 Login...</h2>
            <p>Please wait, you are being redirected.</p>
        </div>
    );
};

export default OAuth2RedirectHandler;