import React, { useState } from 'react';
import { register } from '../api';
import { useNavigate } from 'react-router-dom';

const Registration = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        userName: '',
        userEmail: '',
        userPhoneNo: '',
        userPassword: '',
        userGender: '',
        userAddress: '',
        roles: ['USER'],
    });

    const [message, setMessage] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await register(formData);
            setMessage(response.data);
            // Assuming the backend returns a success status
            if (response.status === 200) {
                navigate('/login');
            }
        } catch (error) {
            setMessage('Registration failed. Please try again.');
            console.error('Registration error:', error);
        }
    };

    return (
        <div style={formContainerStyle}>
            <h2>Register</h2>
            {message && <p style={messageStyle}>{message}</p>}
            <form onSubmit={handleSubmit}>
                <div style={inputGroupStyle}>
                    <label>Username:</label>
                    <input type="text" name="userName" value={formData.userName} onChange={handleChange} required style={inputStyle} />
                </div>
                <div style={inputGroupStyle}>
                    <label>Email:</label>
                    <input type="email" name="userEmail" value={formData.userEmail} onChange={handleChange} required style={inputStyle} />
                </div>
                <div style={inputGroupStyle}>
                    <label>Phone Number:</label>
                    <input type="tel" name="userPhoneNo" value={formData.userPhoneNo} onChange={handleChange} required style={inputStyle} />
                </div>
                <div style={inputGroupStyle}>
                    <label>Password:</label>
                    <input type="password" name="userPassword" value={formData.userPassword} onChange={handleChange} required style={inputStyle} />
                </div>
                <div style={inputGroupStyle}>
                    <label>Gender:</label>
                    <select name="userGender" value={formData.userGender} onChange={handleChange} required style={inputStyle}>
                        <option value="">Select Gender</option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                        <option value="Other">Other</option>
                    </select>
                </div>
                <div style={inputGroupStyle}>
                    <label>Address:</label>
                    <input type="text" name="userAddress" value={formData.userAddress} onChange={handleChange} required style={inputStyle} />
                </div>
                <button type="submit" style={buttonStyle}>Register</button>
            </form>
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

export default Registration;
