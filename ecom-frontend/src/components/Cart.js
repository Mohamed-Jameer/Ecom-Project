import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; // ✅ Add this import
import { fetchUserCart, reduceCartItemQuantity, deleteCartItem, addToCart } from '../api';

const Cart = () => {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate(); // ✅ Add this line

    const getCart = async () => {
        try {
            setLoading(true);
            const response = await fetchUserCart();
            setCart(response.data);
            setLoading(false);
        } catch (err) {
            setError('Failed to fetch cart. Please log in or try again.');
            setLoading(false);
            console.error('Cart API Error:', err);
        }
    };

    useEffect(() => {
        getCart();
    }, []);

    const handleUpdateQuantity = async (productId, action) => {
        try {
            let updatedCart;
            if (action === 'increase') {
                const response = await addToCart(productId);
                updatedCart = response.data;
            } else if (action === 'reduce') {
                const response = await reduceCartItemQuantity(productId);
                updatedCart = response.data;
            }
            setCart(updatedCart);
        } catch (err) {
            console.error('Failed to update item quantity', err);
            setError(err.message || 'Failed to update cart. Please try again.');
        }
    };

    const handleDeleteItem = async (productId) => {
        try {
            const response = await deleteCartItem(productId);
            setCart(response.data);
        } catch (err) {
            console.error('Failed to delete item from cart', err);
            setError('Failed to remove item. Please try again.');
        }
    };

    // ✅ Define the function to handle navigation
    const onProceedToCheckout = () => {
        navigate('/checkout');
    };

    if (loading) {
        return <div style={styles.container}>Loading cart...</div>;
    }

    if (error) {
        return <div style={styles.container}><p style={styles.errorText}>{error}</p></div>;
    }

    if (!cart || !cart.cartItems || cart.cartItems.length === 0) {
        return <div style={styles.container}><p style={styles.emptyCartText}>Your cart is empty.</p></div>;
    }

    const cartTotal = cart.cartItems.reduce((total, item) => total + item.totalPrice, 0);

    return (
        <div style={styles.container}>
            <h1 style={styles.header}>Your Shopping Cart</h1>
            <div style={styles.cartList}>
                {cart.cartItems.map((item) => (
                    <div key={item.cartItemId} style={styles.cartItem}>
                        {/* ✅ Display product image */}
                        {item.image && <img src={`data:image/jpeg;base64,${item.image}`} alt={item.productName} style={styles.productImage} />}

                        <div style={styles.itemDetails}>
                            <h3 style={styles.itemName}>{item.productName}</h3>
                            {/* ✅ Display product brand and description */}
                            <p style={styles.itemBrand}>Brand: {item.brand}</p>
                            <p style={styles.itemDescription}>{item.description}</p>
                            <p style={styles.itemPrice}>${item.price.toFixed(2)}</p>
                        </div>

                        <div style={styles.itemActions}>
                            <div style={styles.quantityControl}>
                                <button
                                    onClick={() => handleUpdateQuantity(item.productId, 'reduce')}
                                    style={styles.quantityButton}
                                    disabled={item.quantity <= 1}
                                >
                                    -
                                </button>
                                <span style={styles.itemQuantity}>{item.quantity}</span>
                                <button
                                    onClick={() => handleUpdateQuantity(item.productId, 'increase')}
                                    style={styles.quantityButton}
                                >
                                    +
                                </button>
                            </div>
                            <button
                                onClick={() => handleDeleteItem(item.productId)}
                                style={styles.removeButton}
                            >
                                Remove
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            <div style={styles.cartSummary}>
                <h2 style={styles.summaryTitle}>Cart Summary</h2>
                <p style={styles.summaryTotal}>Total: **${cartTotal.toFixed(2)}**</p>
                <button style={styles.checkoutButton} onClick={onProceedToCheckout}>Proceed to Checkout</button>
            </div>
        </div>
    );
};

// ✅ Add new styles for image and description
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
    emptyCartText: {
        textAlign: 'center',
        fontSize: '1.5rem',
        color: '#888',
    },
    cartList: {
        display: 'flex',
        flexDirection: 'column',
        gap: '20px',
    },
    cartItem: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '1.5rem',
        border: '1px solid #e0e0e0',
        borderRadius: '10px',
        backgroundColor: '#fff',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.05)',
    },
    productImage: { // ✅ New style for product image
        width: '100px',
        height: '100px',
        objectFit: 'cover',
        marginRight: '1.5rem',
        borderRadius: '5px',
    },
    itemDetails: {
        flex: '1',
    },
    itemName: {
        fontSize: '1.2rem',
        marginBottom: '0.5rem',
    },
    itemPrice: {
        fontSize: '1.1rem',
        color: '#28a745',
        fontWeight: 'bold',
    },
    itemBrand: { // ✅ New style for brand
        fontSize: '0.9rem',
        color: '#555',
        marginTop: '0.5rem',
    },
    itemDescription: { // ✅ New style for description
        fontSize: '0.9rem',
        color: '#777',
        marginTop: '0.2rem',
        fontStyle: 'italic',
    },
    itemActions: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '10px',
    },
    quantityControl: {
        display: 'flex',
        alignItems: 'center',
        gap: '10px',
    },
    quantityButton: {
        background: '#f1f1f1',
        border: '1px solid #ccc',
        borderRadius: '5px',
        width: '30px',
        height: '30px',
        fontSize: '1.2rem',
        cursor: 'pointer',
    },
    itemQuantity: {
        fontSize: '1.1rem',
        fontWeight: 'bold',
    },
    removeButton: {
        background: '#dc3545',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        padding: '8px 12px',
        cursor: 'pointer',
    },
    cartSummary: {
        marginTop: '2rem',
        padding: '2rem',
        border: '1px solid #e0e0e0',
        borderRadius: '10px',
        backgroundColor: '#f8f9fa',
        textAlign: 'right',
    },
    summaryTitle: {
        fontSize: '1.5rem',
        marginBottom: '1rem',
    },
    summaryTotal: {
        fontSize: '1.8rem',
        fontWeight: 'bold',
        color: '#333',
        marginBottom: '1.5rem',
    },
    checkoutButton: {
        padding: '1rem 2rem',
        fontSize: '1.2rem',
        background: '#28a745',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontWeight: 'bold',
    },
};

export default Cart;