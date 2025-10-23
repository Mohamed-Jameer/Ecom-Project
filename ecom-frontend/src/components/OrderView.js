import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';

const OrderView = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const orderId = location.state?.orderId; // passed from Dashboard view

    useEffect(() => {
        if (!orderId) {
            setError('No order selected.');
            setLoading(false);
            return;
        }

        const fetchOrder = async () => {
            try {
                setLoading(true);
                const token = localStorage.getItem('jwtToken');
                const response = await axios.get(`http://localhost:8080/api/orders/my`, {
                    headers: { Authorization: `Bearer ${token}` }
                });

                // Find the order with the selected ID
                const userOrder = response.data.find(o => o.orderId === orderId);
                if (userOrder) {
                    setOrder(userOrder);
                } else {
                    setError('Order not found.');
                }
                setLoading(false);
            } catch (err) {
                console.error(err);
                setError('Failed to fetch order details.');
                setLoading(false);
            }
        };

        fetchOrder();
    }, [orderId]);

    if (loading) return <div style={styles.container}>Loading order...</div>;
    if (error) return <div style={styles.container}><p style={styles.errorText}>{error}</p></div>;

    // Format order date
    const formattedDate = new Date(order.orderDate).toLocaleDateString('en-US', {
        year: 'numeric', month: 'long', day: 'numeric'
    });

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h1 style={styles.header}>Order Details</h1>
                <p><strong>Order ID:</strong> {order.orderId}</p>
                <p><strong>Order Date:</strong> {formattedDate}</p>
                <p><strong>Status:</strong> {order.status}</p>
                <p><strong>Total Amount:</strong> ${order.totalAmount.toFixed(2)}</p>

                <h3>Items:</h3>
                <ul style={styles.list}>
                    {order.orderItems.map(item => (
                        <li key={item.orderItemId} style={styles.listItem}>
                            {item.productName || `Product ID: ${item.productId}`} - Qty: {item.quantity} - Price: ${item.totalPrice.toFixed(2)}
                        </li>
                    ))}
                </ul>

                <div style={styles.buttonGroup}>
                    <button style={styles.button} onClick={() => navigate('/dashboard')}>Back to Dashboard</button>
                    <button style={{ ...styles.button, ...styles.continueButton }} onClick={() => navigate('/')}>Continue Shopping</button>
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh', fontFamily: 'Arial, sans-serif', padding: '2rem' },
    card: { background: '#fff', padding: '2rem', borderRadius: '8px', boxShadow: '0 4px 8px rgba(0,0,0,0.1)', maxWidth: '600px', width: '100%' },
    header: { fontSize: '2rem', color: '#007bff', marginBottom: '1rem' },
    list: { listStyle: 'none', padding: 0 },
    listItem: { padding: '8px 0', borderBottom: '1px solid #e0e0e0' },
    buttonGroup: { marginTop: '1.5rem', display: 'flex', gap: '1rem', justifyContent: 'center' },
    button: { padding: '10px 20px', cursor: 'pointer', borderRadius: '5px', border: 'none', backgroundColor: '#007bff', color: '#fff', fontWeight: 'bold' },
    continueButton: { backgroundColor: '#28a745' },
    errorText: { color: 'red', fontWeight: 'bold', textAlign: 'center' },
};

export default OrderView;
