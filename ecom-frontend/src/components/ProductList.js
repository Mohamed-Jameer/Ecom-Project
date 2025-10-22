import React, { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { fetchAllProducts, searchProducts } from '../api';
import useCart from '../hooks/useCart';

const ProductList = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchParams, setSearchParams] = useSearchParams();
    const [keyword, setKeyword] = useState('');
    const { handleAddToCart } = useCart();
    const [addToCartMessage, setAddToCartMessage] = useState(null);
    const [messageProductId, setMessageProductId] = useState(null);

    useEffect(() => {
        const fetchProducts = async () => {
            const searchQuery = searchParams.get('search');

            try {
                let response;
                setLoading(true);
                if (searchQuery) {
                    // If a search query exists in the URL, perform a search
                    response = await searchProducts(searchQuery);
                    setKeyword(searchQuery);
                } else {
                    // Otherwise, fetch all products
                    response = await fetchAllProducts();
                    setKeyword(''); // Clear the search bar
                }
                setProducts(response.data);
                setLoading(false);
            } catch (err) {
                setError('Failed to fetch products. Please check the server connection.');
                setLoading(false);
                console.error('API Error:', err);
            }
        };

        fetchProducts();
    }, [searchParams]); // Re-run this effect when URL search parameters change

    // Function to handle manual search from the search bar
    const handleSearch = async (e) => {
        e.preventDefault();
        // Update URL search parameters, which will trigger the useEffect hook
        setSearchParams({ search: keyword });
    };

    // New function to handle adding to cart with UI feedback
    const handleUserAddToCart = async (productId, productName) => {
        await handleAddToCart(productId);
        setAddToCartMessage(`"${productName}" added!`);
        setMessageProductId(productId);

        // Clear the message after 3 seconds
        setTimeout(() => {
            setAddToCartMessage(null);
            setMessageProductId(null);
        }, 3000);
    };

    if (loading) {
        return <div style={styles.container}>Loading products...</div>;
    }

    if (error) {
        return <div style={styles.container}><p style={styles.errorText}>{error}</p></div>;
    }

    return (
        <div style={styles.container}>
            <h1 style={styles.header}>Our Products</h1>
            <p style={styles.subtitle}>Browse our complete collection of items below.</p>

            {/* Search Bar */}
            <form onSubmit={handleSearch} style={styles.searchForm}>
                <input
                    type="text"
                    placeholder="Search products..."
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                    style={styles.searchInput}
                />
                <button type="submit" style={styles.searchButton}>Search</button>
            </form>

            <div style={styles.productListGrid}>
                {products.length > 0 ? (
                    products.map(product => (
                        <div key={product.id} style={styles.productCard}>
                            <Link to={`/products/${product.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                <div style={styles.productImageContainer}>
                                    <img src={product.imageUrl} alt={product.name} style={styles.productImage} />
                                </div>
                                <h3 style={styles.productName}>{product.name}</h3>
                                <p style={styles.productPrice}>${product.price.toFixed(2)}</p>
                                <p style={styles.productDescription}>
                                    {product.description.length > 100
                                        ? product.description.substring(0, 100) + '...'
                                        : product.description}
                                </p>
                                <p style={styles.productCategory}>Category: {product.category}</p>
                            </Link>
                            <button
                                style={styles.addToCartButton}
                                onClick={() => handleUserAddToCart(product.id, product.name)}
                            >
                                Add to Cart
                            </button>
                            {/* Display message directly below the button */}
                            {messageProductId === product.id && (
                                <p style={styles.messageBox}>{addToCartMessage}</p>
                            )}
                        </div>
                    ))
                ) : (
                    <p style={styles.noProductsText}>No products found. Please add products to your database.</p>
                )}
            </div>
        </div>
    );
};

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
        marginBottom: '0.5rem',
    },
    subtitle: {
        fontSize: '1.2rem',
        textAlign: 'center',
        color: '#666',
        marginBottom: '2rem',
    },
    // New styles for the search bar
    searchForm: {
        display: 'flex',
        justifyContent: 'center',
        marginBottom: '2rem',
        gap: '10px',
    },
    searchInput: {
        padding: '10px 15px',
        width: '400px',
        borderRadius: '5px',
        border: '1px solid #ccc',
        fontSize: '1rem',
    },
    searchButton: {
        padding: '10px 20px',
        backgroundColor: '#007bff',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '1rem',
    },
    // End of new styles
    messageBox: {
        backgroundColor: '#d4edda',
        color: '#155724',
        border: '1px solid #c3e6cb',
        borderRadius: '5px',
        padding: '0.5rem',
        textAlign: 'center',
        marginTop: '0.5rem',
        fontSize: '0.9rem',
        fontWeight: 'bold',
        width: '100%',
    },
    productListGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
        gap: '25px',
    },
    productCard: {
        border: '1px solid #e0e0e0',
        borderRadius: '10px',
        padding: '1.5rem',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        backgroundColor: '#fff',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.05)',
        transition: 'transform 0.2s, box-shadow 0.2s',
    },
    productImageContainer: {
        width: '100%',
        height: '200px',
        overflow: 'hidden',
        borderRadius: '8px',
        marginBottom: '1rem',
    },
    productImage: {
        width: '100%',
        height: '100%',
        objectFit: 'cover',
    },
    productName: {
        fontSize: '1.4rem',
        color: '#007bff',
        marginBottom: '0.5rem',
        fontWeight: 'bold',
    },
    productPrice: {
        fontSize: '1.3rem',
        color: '#28a745',
        marginBottom: '1rem',
        fontWeight: 'bold',
    },
    productDescription: {
        fontSize: '0.9rem',
        color: '#555',
        marginBottom: '1rem',
        lineHeight: '1.4',
    },
    productCategory: {
        fontSize: '0.9rem',
        color: '#888',
        fontStyle: 'italic',
        marginBottom: '1rem',
    },
    addToCartButton: {
        width: '100%',
        padding: '10px 15px',
        backgroundColor: '#ffc107',
        color: '#333',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '1rem',
        fontWeight: 'bold',
        transition: 'background-color 0.3s',
    },
    errorText: {
        color: 'red',
        textAlign: 'center',
        fontWeight: 'bold',
    },
    noProductsText: {
        textAlign: 'center',
        fontSize: '1.2rem',
        color: '#888',
        gridColumn: '1 / -1',
    }
};

export default ProductList;
