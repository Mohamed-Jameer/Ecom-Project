import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchFeaturedProducts, fetchProductCategories } from '../api';

const Home = () => {
    const navigate = useNavigate();
    const [featuredProducts, setFeaturedProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [loadingCategories, setLoadingCategories] = useState(true);
    const [error, setError] = useState(null);
    const [categoriesError, setCategoriesError] = useState(null);
    const [searchKeyword, setSearchKeyword] = useState('');

    useEffect(() => {
        const getFeaturedProducts = async () => {
            try {
                const response = await fetchFeaturedProducts();
                setFeaturedProducts(response.data);
                setLoading(false);
            } catch (err) {
                setError('Failed to fetch featured products.');
                setLoading(false);
            }
        };

        const getCategories = async () => {
            try {
                const response = await fetchProductCategories();
                setCategories(response.data);
                setLoadingCategories(false);
            } catch (err) {
                setCategoriesError('Failed to fetch categories.');
                setLoadingCategories(false);
            }
        };

        getFeaturedProducts();
        getCategories();
    }, []);

    const handleShopNowClick = () => {
        navigate('/products');
    };

    const handleCategoryClick = (category) => {
        navigate(`/products?category=${encodeURIComponent(category)}`);
    };

    const handleHomeSearch = (e) => {
        e.preventDefault();
        if (searchKeyword.trim()) {
            navigate(`/products?search=${encodeURIComponent(searchKeyword)}`);
        }
    };

    return (
        <div style={styles.container}>
            {/* ✅ Updated Hero Section with Search Bar */}
            <section style={styles.heroSection}>
                <div style={styles.heroText}>
                    <h1 style={styles.heroTitle}>Discover Your Next Favorite Thing</h1>
                    <p style={styles.heroSubtitle}>Explore our curated collection of high-quality products.</p>

                    {/* ✅ Home page search form */}
                    <form onSubmit={handleHomeSearch} style={styles.heroSearchForm}>
                        <input
                            type="text"
                            placeholder="Search products..."
                            value={searchKeyword}
                            onChange={(e) => setSearchKeyword(e.target.value)}
                            style={styles.heroSearchInput}
                        />
                        <button type="submit" style={styles.heroSearchButton}>Search</button>
                    </form>

                </div>
            </section>

            {/* Featured Products Section */}
            <section style={styles.featuredSection}>
                <h2 style={styles.sectionTitle}>Featured Products</h2>
                <div style={styles.productList}>
                    {loading && <p>Loading products...</p>}
                    {error && <p style={{ color: 'red' }}>{error}</p>}
                    {!loading && featuredProducts.length === 0 && <p>No featured products available.</p>}
                    {featuredProducts.map((product) => (
                        <div key={product.id} style={styles.productCard}>
                            <h3 style={styles.productName}>{product.name}</h3>
                            <p style={styles.productPrice}>${product.price.toFixed(2)}</p>
                            <p style={styles.productDescription}>{product.description.substring(0, 70)}...</p>
                            <button style={styles.addToCartButton}>Add to Cart</button>
                        </div>
                    ))}
                </div>
            </section>

            {/* Categories Section */}
            <section style={styles.categoriesSection}>
                <h2 style={styles.sectionTitle}>Shop by Category</h2>
                <div style={styles.categoryList}>
                    {loadingCategories && <p>Loading categories...</p>}
                    {categoriesError && <p style={{ color: 'red' }}>{categoriesError}</p>}
                    {!loadingCategories && categories.length === 0 && <p>No categories found.</p>}
                    {categories.map((category) => (
                        <div
                            key={category}
                            style={styles.categoryCard}
                            onClick={() => handleCategoryClick(category)}
                        >
                            {category}
                        </div>
                    ))}
                </div>
            </section>

            {/* Value Proposition Section */}
            <section style={styles.valuePropsSection}>
                <div style={styles.valuePropCard}>
                    <h3>📦 Fast Shipping</h3>
                    <p>Get your order delivered in record time.</p>
                </div>
                <div style={styles.valuePropCard}>
                    <h3>🔒 Secure Payments</h3>
                    <p>Your transactions are 100% secure with us.</p>
                </div>
                <div style={styles.valuePropCard}>
                    <h3>✨ High Quality</h3>
                    <p>We source only the best products for you.</p>
                </div>
            </section>
        </div>
    );
};

const styles = {
    container: {
        fontFamily: 'Arial, sans-serif',
    },
    heroSection: {
        background: 'url("https://via.placeholder.com/1500x500") no-repeat center center/cover',
        height: '500px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: '#fff',
        textAlign: 'center',
    },
    heroText: {
        background: 'rgba(0, 0, 0, 0.5)',
        padding: '2rem 4rem',
        borderRadius: '10px',
    },
    heroTitle: {
        fontSize: '3rem',
        marginBottom: '1rem',
    },
    heroSubtitle: {
        fontSize: '1.5rem',
        marginBottom: '2rem',
    },
    heroButton: {
        padding: '1rem 2rem',
        fontSize: '1.2rem',
        background: '#007bff',
        color: '#fff',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    // ✅ New styles for the home page search bar
    heroSearchForm: {
        display: 'flex',
        justifyContent: 'center',
        marginTop: '20px',
        gap: '10px',
    },
    heroSearchInput: {
        padding: '10px 15px',
        width: '400px',
        borderRadius: '5px',
        border: '1px solid #ccc',
        fontSize: '1rem',
    },
    heroSearchButton: {
        padding: '10px 20px',
        backgroundColor: '#ffc107',
        color: 'black',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '1rem',
    },
    // ✅ End of new styles
    featuredSection: {
        padding: '3rem 2rem',
        textAlign: 'center',
    },
    sectionTitle: {
        fontSize: '2.5rem',
        marginBottom: '2rem',
        color: '#333',
    },
    productList: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))',
        gap: '20px',
    },
    productCard: {
        border: '1px solid #ccc',
        borderRadius: '10px',
        padding: '1rem',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    productName: {
        fontSize: '1.2rem',
    },
    productPrice: {
        color: '#28a745',
        fontSize: '1.1rem',
        fontWeight: 'bold',
    },
    productDescription: {
        color: '#666',
        fontSize: '0.9rem',
    },
    addToCartButton: {
        background: '#ffc107',
        border: 'none',
        padding: '0.5rem 1rem',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    categoriesSection: {
        padding: '3rem 2rem',
        textAlign: 'center',
        background: '#f8f9fa',
    },
    categoryList: {
        display: 'flex',
        justifyContent: 'center',
        gap: '20px',
        flexWrap: 'wrap',
    },
    categoryCard: {
        background: '#e9ecef',
        padding: '2rem',
        borderRadius: '10px',
        minWidth: '150px',
        fontWeight: 'bold',
        color: '#555',
        cursor: 'pointer',
    },
    valuePropsSection: {
        padding: '3rem 2rem',
        textAlign: 'center',
        display: 'flex',
        justifyContent: 'space-around',
        flexWrap: 'wrap',
        gap: '20px',
    },
    valuePropCard: {
        flex: '1',
        minWidth: '250px',
        padding: '1.5rem',
        borderRadius: '10px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        backgroundColor: '#fff',
    },
};

export default Home;