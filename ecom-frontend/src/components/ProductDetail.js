import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { fetchProductDetails, fetchProductsByCategory } from '../api';
import useCart from '../hooks/useCart';

const ProductDetail = () => {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [relatedProducts, setRelatedProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { handleAddToCart } = useCart();
    const [quantity, setQuantity] = useState(1);
    const [message, setMessage] = useState(null);

    useEffect(() => {
        const getProductDetails = async () => {
            try {
                // Fetch the product details first
                const data = await fetchProductDetails(id);
                setProduct(data);

                if (data.category) {
                    // Fetch all products in the same category
                    const relatedData = await fetchProductsByCategory(data.category);

                    // Filter out the current product from the related list
                    // The API returns the data directly as an array, not inside a 'content' property.
                    const filteredRelated = relatedData.filter(
                        (p) => p.id !== id
                    );
                    setRelatedProducts(filteredRelated);
                }
            } catch (err) {
                setError('Failed to fetch product details. The product may not exist.');
                console.error('API Error:', err);
            } finally {
                setLoading(false);
            }
        };

        getProductDetails();
    }, [id]);

    const handleAddClick = async () => {
        try {
            await handleAddToCart(product.id, quantity);
            setMessage(`Added ${quantity} x "${product.name}" to cart!`);
            setTimeout(() => {
                setMessage(null);
            }, 3000);
        } catch (err) {
            setMessage('Failed to add to cart. Please try again.');
        }
    };

    if (loading) {
        return <div style={styles.container}>Loading product details...</div>;
    }

    if (error) {
        return <div style={styles.container}><p style={styles.errorText}>{error}</p></div>;
    }

    if (!product) {
        return <div style={styles.container}><p style={styles.noProductText}>Product not found.</p></div>;
    }

    return (
        <div style={styles.container}>
            <div style={styles.detailCard}>
                <div style={styles.imageContainer}>
                    <img src={product.imageUrl} alt={product.name} style={styles.productImage} />
                </div>
                <div style={styles.infoContainer}>
                    <h1 style={styles.productName}>{product.name}</h1>
                    <p style={styles.productCategory}>Category: {product.category}</p>
                    <p style={styles.productPrice}>${product.price.toFixed(2)}</p>
                    <p style={styles.productDescription}>{product.description}</p>

                    {message && <div style={styles.messageBox}>{message}</div>}

                    <div style={styles.cartControls}>

                        <button
                            style={styles.addToCartButton}
                            onClick={handleAddClick}
                        >
                            Add to Cart
                        </button>
                    </div>

                    <Link to="/products" style={styles.backLink}>← Back to Products</Link>
                </div>
            </div>

            {relatedProducts.length > 0 && (
                <div style={styles.relatedSection}>
                    <h2 style={styles.sectionTitle}>Related Products</h2>
                    <div style={styles.relatedGrid}>
                        {relatedProducts.map(relatedProduct => (
                            <Link key={relatedProduct.id} to={`/products/${relatedProduct.id}`} style={styles.relatedCard}>
                                <img src={relatedProduct.imageUrl} alt={relatedProduct.name} style={styles.relatedImage} />
                                <h3 style={styles.relatedName}>{relatedProduct.name}</h3>
                                <p style={styles.relatedPrice}>${relatedProduct.price.toFixed(2)}</p>
                                <button
                                    style={styles.relatedAddButton}
                                    onClick={(e) => {
                                        e.preventDefault(); // Prevent navigating to the link
                                        handleAddToCart(relatedProduct.id, 1);
                                    }}
                                >
                                    Add to Cart
                                </button>
                            </Link>
                        ))}
                    </div>
                </div>
            )}
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
    detailCard: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        backgroundColor: '#fff',
        borderRadius: '15px',
        boxShadow: '0 8px 16px rgba(0, 0, 0, 0.1)',
        overflow: 'hidden',
        padding: '2rem',
    },
    imageContainer: {
        width: '100%',
        height: '400px',
        borderRadius: '10px',
        overflow: 'hidden',
        marginBottom: '2rem',
    },
    productImage: {
        width: '100%',
        height: '100%',
        objectFit: 'cover',
    },
    infoContainer: {
        width: '100%',
    },
    productName: {
        fontSize: '2.5rem',
        color: '#333',
        marginBottom: '0.5rem',
        fontWeight: 'bold',
    },
    productCategory: {
        fontSize: '1rem',
        color: '#888',
        fontStyle: 'italic',
        marginBottom: '1rem',
    },
    productPrice: {
        fontSize: '2rem',
        color: '#28a745',
        marginBottom: '1rem',
        fontWeight: 'bold',
    },
    productDescription: {
        fontSize: '1rem',
        color: '#555',
        lineHeight: '1.6',
        marginBottom: '2rem',
    },
    cartControls: {
        display: 'flex',
        gap: '1rem',
        marginBottom: '1rem',
        alignItems: 'center',
    },
    quantityInput: {
        width: '80px',
        padding: '10px',
        borderRadius: '8px',
        border: '1px solid #ccc',
        textAlign: 'center',
    },
    addToCartButton: {
        flex: 1,
        padding: '12px 20px',
        backgroundColor: '#ffc107',
        color: '#333',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '1.1rem',
        fontWeight: 'bold',
        transition: 'background-color 0.3s',
    },
    backLink: {
        color: '#007bff',
        textDecoration: 'none',
        fontSize: '1rem',
        transition: 'color 0.3s',
    },
    errorText: {
        color: 'red',
        textAlign: 'center',
        fontWeight: 'bold',
    },
    noProductText: {
        textAlign: 'center',
        fontSize: '1.2rem',
        color: '#888',
    },
    messageBox: {
        backgroundColor: '#d4edda',
        color: '#155724',
        border: '1px solid #c3e6cb',
        borderRadius: '5px',
        padding: '1rem',
        textAlign: 'center',
        marginBottom: '1.5rem',
        fontSize: '1rem',
        fontWeight: 'bold',
    },
    relatedSection: {
        marginTop: '3rem',
        textAlign: 'center',
    },
    sectionTitle: {
        fontSize: '2rem',
        marginBottom: '2rem',
        borderBottom: '2px solid #ccc',
        paddingBottom: '0.5rem',
        display: 'inline-block',
    },
    relatedGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))',
        gap: '20px',
    },
    relatedCard: {
        textDecoration: 'none',
        color: 'inherit',
        border: '1px solid #ddd',
        borderRadius: '8px',
        padding: '15px',
        textAlign: 'center',
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
        transition: 'transform 0.2s',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    relatedImage: {
        width: '100%',
        height: '150px',
        objectFit: 'cover',
        borderRadius: '5px',
        marginBottom: '10px',
    },
    relatedName: {
        fontSize: '1.2rem',
        fontWeight: 'bold',
        margin: '0',
    },
    relatedPrice: {
        fontSize: '1.1rem',
        color: '#28a745',
        fontWeight: 'bold',
        marginTop: '5px',
    },
    relatedAddButton: {
        marginTop: '10px',
        width: '100%',
        padding: '10px',
        backgroundColor: '#ffc107',
        color: '#333',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '1rem',
        fontWeight: 'bold',
        transition: 'background-color 0.3s',
    }
};

export default ProductDetail;
