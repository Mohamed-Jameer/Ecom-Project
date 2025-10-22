import React, { useState } from 'react';
import { login } from '../api';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const navigate = useNavigate();
    const [userEmail, setUserEmail] = useState('');
    const [userPassword, setUserPassword] = useState('');
    const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
      e.preventDefault();
      try {
          const response = await login(userEmail, userPassword);
          const token = response.data.token; // your backend must return { token: "..." }

          // Save JWT to localStorage
          localStorage.setItem('jwtToken', token);

          // Dispatch login event BEFORE navigation
          window.dispatchEvent(new Event('login'));

          // Navigate to dashboard after updating storage
          navigate('/dashboard');
      } catch (error) {
          setMessage('Login failed');
          console.error(error);
      }
  };



    const handleGoogleLogin = () => {
        // Redirects to your Spring Boot's Google OAuth2 endpoint
        // Make sure this URL matches your backend configuration
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };

    return (
        <div style={formContainerStyle}>
            <h2>Login</h2>
            {message && <p style={messageStyle}>{message}</p>}
            <form onSubmit={handleSubmit}>
                <div style={inputGroupStyle}>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={userEmail}
                        onChange={(e) => setUserEmail(e.target.value)}
                        required
                        style={inputStyle}
                    />
                </div>
                <div style={inputGroupStyle}>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={userPassword}
                        onChange={(e) => setUserPassword(e.target.value)}
                        required
                        style={inputStyle}
                    />
                </div>
                <button type="submit" style={buttonStyle}>Login</button>
            </form>

            <div style={{ textAlign: 'center', margin: '20px 0' }}>
                <p>OR</p>
                <button onClick={handleGoogleLogin} style={googleButtonStyle}>
                    Login with Google
                </button>
            </div>
        </div>
    );
};

const formContainerStyle = {
    maxWidth: '400px',
    margin: 'auto',
    padding: '20px',
    border: '1px solid #ccc',
    borderRadius: '8px'
};
const inputGroupStyle = { marginBottom: '15px' };
const inputStyle = { width: '100%', padding: '8px' };
const buttonStyle = {
    width: '100%',
    padding: '10px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer'
};
const messageStyle = { color: 'red' };
const googleButtonStyle = {
    width: '100%',
    padding: '10px',
    backgroundColor: '#db4437', // Google's brand color
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
};














export default Login;
