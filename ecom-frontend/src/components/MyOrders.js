import React, { useState, useEffect } from 'react';
import { fetchOrders } from '../api'; // Ensure this path is correct

const MyOrders = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const getOrders = async () => {
            try {
                setLoading(true);
                const data = await fetchOrders();

                // Add a defensive check to ensure the data is an array
                if (Array.isArray(data)) {
                    setOrders(data);
                } else {
                    console.error("API response is not a valid array:", data);
                    setOrders([]); // Set to an empty array to prevent the error
                }

                setLoading(false);
            } catch (err) {
                setError('Failed to fetch your orders. Please try logging in again.');
                setLoading(false);
                console.error('Orders API Error:', err);
            }
        };

        getOrders();
    }, []);

    if (loading) {
        return <div style={styles.container}>Loading your orders...</div>;
    }

    if (error) {
        return <div style={styles.container}><p style={styles.errorText}>{error}</p></div>;
    }

    if (orders.length === 0) {
        return <div style={styles.container}><p style={styles.noOrdersText}>You have not placed any orders yet.</p></div>;
    }


    return (
        <div style={styles.container}>
            <h1 style={styles.header}>My Orders</h1>
            <div style={styles.ordersList}>
                {orders.map(order => (
                    // ✅ Add a check for order existence
                    order && (
                        <div key={order.orderId} style={styles.orderCard}>
                            <div style={styles.orderHeader}>
                                <h3 style={styles.orderId}>Order ID: {order.orderId}</h3>
                                <p style={styles.orderDate}>Date: {new Date(order.orderDate).toLocaleDateString()}</p>
                            </div>
                            <ul style={styles.itemsList}>
                                {order.orderItems && order.orderItems.map(item => (
                                    // ✅ Add a check for item existence and its totalPrice property
                                    item && (
                                        <li key={item.orderItemId} style={styles.item}>
                                            <span style={styles.itemName}>{item.productName}</span>
                                            <span style={styles.itemQuantity}>Quantity: {item.quantity}</span>
                                            <span style={styles.itemPrice}>
                                                {/* Use optional chaining to safely access totalPrice */}
                                                ${item.totalPrice?.toFixed(2) || '0.00'}
                                            </span>
                                        </li>
                                    )
                                ))}
                            </ul>
                            <div style={styles.orderSummary}>
                                <p style={styles.totalPrice}>
                                    Total Price: **${order.totalPrice?.toFixed(2) || '0.00'}**
                                </p>
                            </div>
                        </div>
                    )
                ))}
            </div>
        </div>
    );
};
const styles = {
    container: {
        padding: '2rem',
        maxWidth: '900px',
        margin: '0 auto',
        fontFamily: 'Arial, sans-serif',
    },
    header: {
        fontSize: '2.5rem',
        textAlign: 'center',
        color: '#333',
        marginBottom: '2rem',
    },
    errorText: {
        color: 'red',
        textAlign: 'center',
        fontWeight: 'bold',
    },
    noOrdersText: {
        textAlign: 'center',
        fontSize: '1.5rem',
        color: '#888',
    },
    ordersList: {
        display: 'flex',
        flexDirection: 'column',
        gap: '20px',
    },
    orderCard: {
        border: '1px solid #e0e0e0',
        borderRadius: '10px',
        padding: '1.5rem',
        backgroundColor: '#fff',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.05)',
    },
    orderHeader: {
        display: 'flex',
        justifyContent: 'space-between',
        borderBottom: '1px solid #eee',
        paddingBottom: '1rem',
        marginBottom: '1rem',
    },
    orderId: {
        fontSize: '1.2rem',
        margin: 0,
    },
    orderDate: {
        fontSize: '1rem',
        color: '#777',
        margin: 0,
    },
    itemsList: {
        listStyle: 'none',
        padding: 0,
        margin: 0,
    },
    item: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '0.75rem 0',
        borderBottom: '1px dotted #e0e0e0',
    },
    itemName: {
        fontWeight: 'bold',
        flex: 1,
    },
    itemQuantity: {
        color: '#555',
        flex: 0.3,
    },
    itemPrice: {
        color: '#28a745',
        fontWeight: 'bold',
    },
    orderSummary: {
        textAlign: 'right',
        marginTop: '1.5rem',
    },
    totalPrice: {
        fontSize: '1.5rem',
        color: '#333',
        margin: 0,
    },
};

export default MyOrders;