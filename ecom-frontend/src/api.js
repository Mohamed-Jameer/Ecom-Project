import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_URL,
});

// Add a request interceptor to include the JWT token in headers
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);



export const fetchProducts = async (page = 0, size = 10) => {
    const response = await api.get(`/products?page=${page}&size=${size}`);
    return response.data;
};

export const fetchProductDetails = async (id) => {
    const response = await api.get(`/products/${id}`);
    return response.data;
};

// ✅ Corrected: Changed query parameter from 'query' to 'keyword' to match the backend
export const fetchProductsBySearch = async (query) => {
    const response = await api.get(`/products/search?keyword=${query}`);
    return response.data;
};


// Note: This endpoint is not implemented in your provided backend ProductController.
// You will need to add a new endpoint on the backend to filter products by category.
export const fetchProductsByCategory = async (category) => {
    const response = await api.get(`/products/category?category=${category}`);
    return response.data;
};





















// Add the new function to fetch dashboard data
export const fetchDashboardData = () => {
    return api.get('/dashboard');
};

export const login = (userEmail, userPassword) => {
    return api.post('/login', { userEmail, userPassword });
};

export const register = (userData) => {
    return api.post('/register', userData);
};

export const fetchFeaturedProducts = () => {
    return api.get('/products/featured');
};

export const fetchAllProducts = () => {
    return api.get('/products');
};

export const fetchProductCategories = () => {
    return api.get('/products/categories');
};

export const searchProducts = (keyword) => {
    return api.get(`/products/search?keyword=${encodeURIComponent(keyword)}`);
};
export const fetchUserCart = () => {
    return api.get('/cart');
};

export const addToCart = (productId) => {
    return api.post(`/cart/add/${productId}`);
};

export const reduceCartItemQuantity = (productId) => {
    return api.post(`/cart/reduce/${productId}`);
};

export const deleteCartItem = (productId) => {
    return api.delete(`/cart/item/${productId}`);
};


// ✅ Corrected: Pass the shipping data and use the 'api' instance
export const placeOrder = async (orderData) => {
    return await api.post('/orders/place', orderData);
};

// ✅ Corrected: Use the 'api' instance and the correct endpoint path
export const fetchOrders = async () => {
    // We are awaiting the API call and getting the full response object
    const response = await api.get('/orders/my');

    // ✅ We return only the 'data' property from the response object
    return response.data;
};