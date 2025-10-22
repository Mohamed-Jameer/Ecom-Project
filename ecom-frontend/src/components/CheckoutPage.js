import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchUserCart, placeOrder } from '../api';

const CheckoutPage = () => {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [placingOrder, setPlacingOrder] = useState(false);
    const [formData, setFormData] = useState({
        fullName: '',
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        postalCode: '',
        country: '',
    });
    const [formErrors, setFormErrors] = useState({});
    const navigate = useNavigate();

    // Fetches the user's cart on component mount
    useEffect(() => {
        const getCart = async () => {
            try {
                setLoading(true);
                const response = await fetchUserCart();
                setCart(response.data);
                setLoading(false);
            } catch (err) {
                setError('Failed to load cart for checkout. Please log in or try again.');
                setLoading(false);
                console.error('Checkout API Error:', err);
            }
        };
        getCart();
    }, []);

    // Handles changes to the form input fields
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Validates the form data
    const validateForm = () => {
        const errors = {};
        if (!formData.fullName) errors.fullName = 'Full Name is required';
        if (!formData.addressLine1) errors.addressLine1 = 'Address Line 1 is required';
        if (!formData.city) errors.city = 'City is required';
        if (!formData.state) errors.state = 'State is required';
        if (!formData.postalCode) errors.postalCode = 'Postal Code is required';
        if (!formData.country) errors.country = 'Country is required';
        setFormErrors(errors);
        return Object.keys(errors).length === 0;
    };

    // Handles the order placement process
    const handlePlaceOrder = async () => {
        if (!validateForm()) {
            return;
        }

        try {
            setPlacingOrder(true);
            setError(null);
            // The placeOrder API call now sends the user's shipping details
            await placeOrder(formData);
            setPlacingOrder(false);
            navigate('/order-confirmation', { state: { orderPlaced: true } });
        } catch (err) {
            setPlacingOrder(false);
            const errorMessage = err.response?.data?.message || 'Failed to place order. Please check your details and try again.';
            setError(errorMessage);
            console.error('Place Order API Error:', err.response || err);
        }
    };

    if (loading) {
        return <div style={styles.container}>Loading checkout summary...</div>;
    }

    if (error && !placingOrder) { // Display a prominent error message if it's not a loading state
        return (
            <div style={styles.container}>
                <p style={styles.errorText}>{error}</p>
                <button onClick={() => navigate('/cart')} style={styles.backButton}>Go back to cart</button>
            </div>
        );
    }

    if (!cart || !cart.cartItems || cart.cartItems.length === 0) {
        return <div style={styles.container}><p style={styles.emptyCartText}>Your cart is empty. Please add items to checkout.</p></div>;
    }

    const cartTotal = cart.cartItems.reduce((total, item) => total + item.totalPrice, 0);

    return (
        <div style={styles.container}>
            <h1 style={styles.header}>Checkout</h1>
            <div style={styles.mainContent}>
                <div style={styles.summaryContainer}>
                    <h2 style={styles.sectionTitle}>Order Summary</h2>
                    {cart.cartItems.map((item) => (
                        <div key={item.cartItemId} style={styles.cartItem}>
                            <img src={`data:image/jpeg;base64,${item.image}`} alt={item.productName} style={styles.productImage} />
                            <div style={styles.itemDetails}>
                                <p style={styles.itemName}>{item.productName}</p>
                                <p style={styles.itemQuantity}>Quantity: {item.quantity}</p>
                                <p style={styles.itemPrice}>${item.price.toFixed(2)} each</p>
                            </div>
                            <p style={styles.itemTotalPrice}>${item.totalPrice.toFixed(2)}</p>
                        </div>
                    ))}
                    <div style={styles.totalSection}>
                        <div style={styles.totalRow}>
                            <span style={styles.totalLabel}>Subtotal:</span>
                            <span style={styles.totalAmount}>${cartTotal.toFixed(2)}</span>
                        </div>
                        <div style={styles.totalRow}>
                            <span style={styles.totalLabel}>Shipping:</span>
                            <span style={styles.totalAmount}>$0.00</span>
                        </div>
                        <div style={styles.totalRow} style={styles.grandTotal}>
                            <span style={styles.totalLabel}>Grand Total:</span>
                            <span style={styles.totalAmount}>${cartTotal.toFixed(2)}</span>
                        </div>
                    </div>
                </div>

                <div style={styles.formContainer}>
                    <h2 style={styles.sectionTitle}>Shipping Information</h2>
                    <form style={styles.form}>
                        <div style={styles.formGroup}>
                            <label style={styles.label}>Full Name</label>
                            <input
                                type="text"
                                name="fullName"
                                value={formData.fullName}
                                onChange={handleInputChange}
                                style={styles.input}
                            />
                            {formErrors.fullName && <p style={styles.formError}>{formErrors.fullName}</p>}
                        </div>
                        <div style={styles.formGroup}>
                            <label style={styles.label}>Address Line 1</label>
                            <input
                                type="text"
                                name="addressLine1"
                                value={formData.addressLine1}
                                onChange={handleInputChange}
                                style={styles.input}
                            />
                            {formErrors.addressLine1 && <p style={styles.formError}>{formErrors.addressLine1}</p>}
                        </div>
                        <div style={styles.formGroup}>
                            <label style={styles.label}>Address Line 2 (Optional)</label>
                            <input
                                type="text"
                                name="addressLine2"
                                value={formData.addressLine2}
                                onChange={handleInputChange}
                                style={styles.input}
                            />
                        </div>
                        <div style={styles.formGroup}>
                            <label style={styles.label}>City</label>
                            <input
                                type="text"
                                name="city"
                                value={formData.city}
                                onChange={handleInputChange}
                                style={styles.input}
                            />
                            {formErrors.city && <p style={styles.formError}>{formErrors.city}</p>}
                        </div>
                        <div style={styles.formGroup}>
                            <label style={styles.label}>State</label>
                            <input
                                type="text"
                                name="state"
                                value={formData.state}
                                onChange={handleInputChange}
                                style={styles.input}
                            />
                            {formErrors.state && <p style={styles.formError}>{formErrors.state}</p>}
                        </div>
                        <div style={styles.formGroup}>
                            <label style={styles.label}>Postal Code</label>
                            <input
                                type="text"
                                name="postalCode"
                                value={formData.postalCode}
                                onChange={handleInputChange}
                                style={styles.input}
                            />
                            {formErrors.postalCode && <p style={styles.formError}>{formErrors.postalCode}</p>}
                        </div>
                        <div style={styles.formGroup}>
                            <label style={styles.label}>Country</label>
                            <input
                                type="text"
                                name="country"
                                value={formData.country}
                                onChange={handleInputChange}
                                style={styles.input}
                            />
                            {formErrors.country && <p style={styles.formError}>{formErrors.country}</p>}
                        </div>
                    </form>
                </div>
            </div>
            <div style={styles.checkoutActions}>
                {error && <p style={styles.errorText}>{error}</p>}
                <button
                    onClick={handlePlaceOrder}
                    style={styles.placeOrderButton}
                    disabled={placingOrder}
                >
                    {placingOrder ? 'Placing Order...' : `Confirm and Pay $${cartTotal.toFixed(2)}`}
                </button>
            </div>
        </div>
    );
};

// ... (Styles) ...
const styles = {
    container: {
        padding: '2rem',
        maxWidth: '1200px',
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
        marginTop: '1rem',
    },
    emptyCartText: {
        textAlign: 'center',
        fontSize: '1.5rem',
        color: '#888',
    },
    backButton: {
        display: 'block',
        margin: '2rem auto',
        padding: '10px 20px',
        backgroundColor: '#f0f0f0',
        border: '1px solid #ccc',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    mainContent: {
        display: 'flex',
        gap: '2rem',
        flexWrap: 'wrap',
    },
    summaryContainer: {
        flex: 1,
        minWidth: '400px',
        border: '1px solid #e0e0e0',
        borderRadius: '10px',
        padding: '1.5rem',
        backgroundColor: '#fff',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.05)',
    },
    formContainer: {
        flex: 1,
        minWidth: '400px',
        border: '1px solid #e0e0e0',
        borderRadius: '10px',
        padding: '1.5rem',
        backgroundColor: '#fff',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.05)',
    },
    sectionTitle: {
        fontSize: '1.5rem',
        borderBottom: '2px solid #eee',
        paddingBottom: '0.5rem',
        marginBottom: '1rem',
    },
    cartItem: {
        display: 'flex',
        alignItems: 'center',
        padding: '1rem 0',
        borderBottom: '1px solid #eee',
    },
    productImage: {
        width: '60px',
        height: '60px',
        objectFit: 'cover',
        marginRight: '1rem',
        borderRadius: '5px',
    },
    itemDetails: {
        flex: 1,
    },
    itemName: {
        margin: 0,
        fontWeight: 'bold',
    },
    itemQuantity: {
        margin: '0.25rem 0 0',
        fontSize: '0.9rem',
        color: '#666',
    },
    itemPrice: {
        margin: '0.25rem 0 0',
        fontSize: '0.9rem',
        color: '#666',
    },
    itemTotalPrice: {
        fontWeight: 'bold',
        fontSize: '1.1rem',
    },
    totalSection: {
        marginTop: '1.5rem',
        borderTop: '2px solid #ccc',
        paddingTop: '1rem',
    },
    totalRow: {
        display: 'flex',
        justifyContent: 'space-between',
        marginBottom: '0.5rem',
    },
    totalLabel: {
        fontSize: '1.1rem',
    },
    totalAmount: {
        fontSize: '1.1rem',
        fontWeight: 'bold',
    },
    grandTotal: {
        fontSize: '1.5rem',
        fontWeight: 'bold',
        paddingTop: '0.5rem',
        borderTop: '1px solid #ddd',
        marginTop: '1rem',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
    },
    formGroup: {
        marginBottom: '1rem',
    },
    label: {
        display: 'block',
        marginBottom: '0.5rem',
        fontWeight: 'bold',
        color: '#555',
    },
    input: {
        width: '100%',
        padding: '0.75rem',
        border: '1px solid #ccc',
        borderRadius: '5px',
        fontSize: '1rem',
        boxSizing: 'border-box',
    },
    formError: {
        color: 'red',
        fontSize: '0.85rem',
        marginTop: '0.25rem',
    },
    checkoutActions: {
        marginTop: '2rem',
        textAlign: 'right',
    },
    placeOrderButton: {
        padding: '1rem 2rem',
        fontSize: '1.2rem',
        background: '#28a745',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontWeight: 'bold',
        transition: 'background-color 0.3s ease',
    },
};

export default CheckoutPage;