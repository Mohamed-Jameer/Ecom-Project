import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';

// Import the reusable components
import Header from './components/Header';
import Footer from './components/Footer';

// Import page components
import Login from './components/Login';
import Registration from './components/Registration';
import Dashboard from './components/Dashboard';
import OAuth2RedirectHandler from './components/OAuth2RedirectHandler';
import Home from './components/Home';
import ProductList from './components/ProductList';
import ProductDetail from './components/ProductDetail';
import Cart from './components/Cart';
import CheckoutPage from './components/CheckoutPage';
import OrderConfirmationPage from './components/OrderConfirmationPage';
import MyOrders from './components/MyOrders'; // ✅ The new import

// This component protects routes by checking for a token
const ProtectedRoute = ({ children }) => {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        // Use <Navigate> for proper routing
        return <Navigate to="/login" />;
    }
    return children;
};

const App = () => {
    const handleLogout = () => {
        localStorage.removeItem('jwtToken');
        window.location.href = '/';
    };

    return (
        <Router>
            {/* Main flex container to push footer to the bottom */}
            <div style={appContainerStyle}>
                {/* Header no longer needs cart props */}
                <Header />
                <main style={mainContentStyle}>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Registration />} />
                        <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />

                        {/* ProductList no longer needs cart handlers */}
                        <Route path="/products" element={<ProductList />} />

                        <Route path="/products/:id" element={<ProductDetail />} />
                        <Route path="/my-orders" element={<MyOrders />} /> {/* ✅ The new route */}

                        {/* Cart no longer needs cart state or handlers */}
                        <Route path="/cart" element={<Cart />} />
                         <Route path="/order-confirmation" element={<OrderConfirmationPage />} />

                        <Route path="/checkout" element={<CheckoutPage />} />

                        <Route
                            path="/dashboard"
                            element={
                                <ProtectedRoute>
                                    <Dashboard />
                                </ProtectedRoute>
                            }
                        />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
};

const appContainerStyle = {
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
};

const mainContentStyle = {
    flex: '1',
    padding: '20px',
};

export default App;
