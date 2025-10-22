import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchDashboardData } from '../api';

const Dashboard = () => {
    const navigate = useNavigate();
    const [dashboardData, setDashboardData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const getDashboardData = async () => {
            try {
                const response = await fetchDashboardData();
                setDashboardData(response.data);
                setLoading(false);
            } catch (err) {
                setError('Failed to load dashboard data. Please try logging in again.');
                setLoading(false);
                console.error('Dashboard data fetch error:', err);
            }
        };
        getDashboardData();
    }, []);

    if (loading) return <div style={styles.container}>Loading dashboard...</div>;
    if (error) return <div style={styles.container}><p style={styles.errorText}>{error}</p></div>;
    if (!dashboardData) return <div style={styles.container}>No dashboard data available.</div>;

    const { userName, userEmail, userPhoneNo, userAddress, cartItems, totalCartItems, cartTotal, recentOrders, recommendedProducts } = dashboardData;

    const handleViewOrder = (orderId) => {
        // Navigate to Order Confirmation / Order Details page
        navigate('/order-confirmation', { state: { orderId, orderPlaced: true } });
    };

    return (
        <div style={styles.container}>
            <h1 style={styles.header}>Welcome, {userName}!</h1>
            <p style={styles.subHeader}>Here is your e-commerce dashboard.</p>

            {/* User Info */}
            <div style={styles.section}>
                <h3 style={styles.sectionHeader}>User Profile</h3>
                <p><strong>Email:</strong> {userEmail}</p>
                <p><strong>Phone:</strong> {userPhoneNo || 'N/A'}</p>
                <p><strong>Address:</strong> {userAddress || 'N/A'}</p>
            </div>

            {/* Shopping Cart */}
            <div style={styles.section}>
                <h3 style={styles.sectionHeader}>Your Shopping Cart ({totalCartItems} items)</h3>
                <p><strong>Total:</strong> ${cartTotal ? cartTotal.toFixed(2) : '0.00'}</p>
                <ul style={styles.list}>
                    {cartItems && cartItems.length > 0 ? cartItems.map(item => (
                        <li key={item.cartItemId} style={styles.listItem}>
                            {item.productName} - Qty: {item.quantity} - Total: ${item.totalPrice.toFixed(2)}
                        </li>
                    )) : <p>Your cart is empty.</p>}
                </ul>
            </div>

            {/* Recent Orders */}
            <div style={styles.section}>
                <h3 style={styles.sectionHeader}>Recent Orders</h3>
                <ul style={styles.list}>
                    {recentOrders && recentOrders.length > 0 ? recentOrders.map(order => (
                        <li key={order.orderId} style={styles.listItem}>
                            Order #{order.orderId} - Status: {order.status} - Total: ${order.totalAmount.toFixed(2)}
                            <button style={styles.viewButton} onClick={() => handleViewOrder(order.orderId)}>
                                View Order
                            </button>
                        </li>
                    )) : <p>No recent orders found.</p>}
                </ul>
            </div>

            {/* Recommended Products */}
            <div style={styles.section}>
                <h3 style={styles.sectionHeader}>Recommended for You</h3>
                <div style={styles.productList}>
                    {recommendedProducts && recommendedProducts.length > 0 ? recommendedProducts.map(product => (
                        <div key={product.id} style={styles.productCard}>
                            <h4 style={styles.productName}>{product.name}</h4>
                            <p style={styles.productPrice}>${product.price.toFixed(2)}</p>
                            <p style={styles.productDescription}>{product.description.substring(0, 50)}...</p>
                        </div>
                    )) : <p>No recommendations available.</p>}
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: { padding: '2rem', maxWidth: '1200px', margin: '0 auto', fontFamily: 'Arial, sans-serif' },
    header: { fontSize: '2.5rem', color: '#333', borderBottom: '2px solid #007bff', paddingBottom: '0.5rem', marginBottom: '1rem' },
    subHeader: { fontSize: '1.2rem', color: '#666', marginBottom: '2rem' },
    section: { marginBottom: '2rem', padding: '1.5rem', backgroundColor: '#f8f9fa', borderRadius: '8px', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' },
    sectionHeader: { fontSize: '1.8rem', color: '#007bff', marginBottom: '1rem' },
    list: { listStyleType: 'none', padding: 0 },
    listItem: { padding: '10px 0', borderBottom: '1px solid #e9ecef', display: 'flex', justifyContent: 'space-between', alignItems: 'center' },
    viewButton: { marginLeft: '10px', padding: '5px 10px', cursor: 'pointer', border: 'none', borderRadius: '4px', backgroundColor: '#28a745', color: '#fff' },
    productList: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '20px' },
    productCard: { border: '1px solid #ccc', borderRadius: '8px', padding: '1rem', textAlign: 'center', backgroundColor: '#fff' },
    productName: { fontSize: '1.2rem', margin: 0 },
    productPrice: { fontSize: '1.1rem', color: '#28a745', margin: '0.5rem 0' },
    productDescription: { fontSize: '0.9rem', color: '#555' },
    errorText: { color: 'red', fontWeight: 'bold' },
};

export default Dashboard;
