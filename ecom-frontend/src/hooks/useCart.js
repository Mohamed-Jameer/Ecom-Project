import { useState, useEffect } from 'react';
import { fetchUserCart, addToCart, deleteCartItem, reduceCartItemQuantity } from '../api';

const useCart = () => {
    const [cartItems, setCartItems] = useState([]);
    const [cartCount, setCartCount] = useState(0);

    const refreshCart = async () => {
        try {
            const token = localStorage.getItem('jwtToken');
            if (!token) {
                setCartItems([]);
                setCartCount(0);
                return;
            }

            const data = await fetchUserCart();
            setCartItems(data.cartItems || []);
            setCartCount(data.cartItems ? data.cartItems.length : 0);
        } catch (error) {
            console.error('Failed to refresh cart:', error);
            setCartItems([]);
            setCartCount(0);
        }
    };

    const handleAddToCart = async (productId) => {
        try {
            await addToCart(productId);
            await refreshCart();
        } catch (error) {
            console.error('Error adding to cart:', error);
        }
    };

    const handleDeleteCartItem = async (productId) => {
        try {
            await deleteCartItem(productId);
            await refreshCart();
        } catch (error) {
            console.error('Error deleting cart item:', error);
        }
    };

    const handleReduceCartItemQuantity = async (productId) => {
        try {
            await reduceCartItemQuantity(productId);
            await refreshCart();
        } catch (error) {
            console.error('Error reducing cart item quantity:', error);
        }
    };

    useEffect(() => {
        refreshCart();

        const handleLoginLogout = () => {
            refreshCart();
        };

        window.addEventListener('login', handleLoginLogout);
        window.addEventListener('logout', handleLoginLogout);

        return () => {
            window.removeEventListener('login', handleLoginLogout);
            window.removeEventListener('logout', handleLoginLogout);
        };
    }, []);

    return {
        cartItems,
        cartCount,
        handleAddToCart,
        handleDeleteCartItem,
        handleReduceCartItemQuantity,
        refreshCart,
    };
};

export default useCart;
