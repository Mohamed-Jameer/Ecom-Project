import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { fetchOrders } from '../api'; // ✅ Use the centralized API function

const OrderConfirmationPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const orderId = location.state?.orderId; // Get orderId from navigation state
        if (!orderId) {
            navigate('/', { replace: true });
            return;
        }

        const getOrderDetails = async () => {
            try {
                setLoading(true);

                // ✅ fetch all user orders from backend
                const orders = await fetchOrders();

                // ✅ find the specific order
                const currentOrder = orders.find(o => o.orderId === orderId);
                if (!currentOrder) throw new Error('Order not found');

                setOrder(currentOrder);
                setLoading(false);
            } catch (err) {
                console.error('Error fetching order:', err);
                setError('Failed to fetch order details. Please check your order history.');
                setLoading(false);
            }
        };

        getOrderDetails();
    }, [location.state, navigate]);

    if (loading) return <div style={styles.container}>Loading order details...</div>;
    if (error) return <div style={styles.container}><p style={styles.errorText}>{error}</p></div>;
    if (!order) return (
        <div style={styles.container}>
            <p style={styles.emptyText}>Order not found.</p>
            <button onClick={() => navigate('/my-orders')} style={styles.button}>Go to My Orders</button>
        </div>
    );

    const formattedDate = new Date(order.orderDate).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
    });

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h1 style={styles.header}>Order Confirmed!</h1>
                <p style={styles.confirmationText}>Thank you for your purchase. Your order has been placed successfully.</p>

                <div style={styles.orderSummary}>
                    <p><strong>Order ID:</strong> {order.orderId}</p>
                    <p><strong>Order Date:</strong> {formattedDate}</p>
                    <p><strong>Order Status:</strong> {order.status}</p>
                    <p style={styles.totalAmount}><strong>Total Amount:</strong> ${order.totalAmount.toFixed(2)}</p>
                </div>

                <div style={styles.itemList}>
                    <h3>Items in your order:</h3>
                    {order.orderItems.map(item => (
                        <div key={item.orderItemId} style={styles.orderItem}>
                            <p>{item.productName || `Product ID: ${item.productId}`}</p>
                            <p>Quantity: {item.quantity}</p>
                            <p>Price: ${item.totalPrice.toFixed(2)}</p>
                        </div>
                    ))}
                </div>

                <button onClick={() => navigate('/my-orders')} style={styles.button}>View All My Orders</button>
                <button onClick={() => navigate('/')} style={{ ...styles.button, ...styles.continueButton }}>Continue Shopping</button>
            </div>
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '80vh',
        padding: '2rem',
        fontFamily: 'Arial, sans-serif',
    },
    card: {
        backgroundColor: '#fff',
        padding: '3rem',
        borderRadius: '10px',
        boxShadow: '0 8px 16px rgba(0, 0, 0, 0.1)',
        textAlign: 'center',
        maxWidth: '600px',
        width: '100%',
    },
    header: { fontSize: '2.5rem', color: '#28a745', marginBottom: '1rem' },
    confirmationText: { fontSize: '1.1rem', color: '#555', marginBottom: '2rem' },
    errorText: { color: 'red', fontSize: '1.2rem', textAlign: 'center' },
    emptyText: { fontSize: '1.2rem', color: '#888', textAlign: 'center' },
    orderSummary: { backgroundColor: '#f8f9fa', border: '1px solid #e9ecef', padding: '1.5rem', borderRadius: '8px', textAlign: 'left', marginBottom: '2rem' },
    totalAmount: { fontSize: '1.5rem', color: '#333', fontWeight: 'bold', marginTop: '1rem' },
    itemList: { textAlign: 'left', marginBottom: '2rem' },
    orderItem: { borderBottom: '1px solid #eee', padding: '0.75rem 0' },
    button: { padding: '1rem 1.5rem', fontSize: '1rem', margin: '0 0.5rem', cursor: 'pointer', border: 'none', borderRadius: '5px', fontWeight: 'bold', transition: 'background-color 0.3s ease' },
    continueButton: { backgroundColor: '#007bff', color: '#fff' },
};

export default OrderConfirmationPage;
