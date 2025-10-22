import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => {
    return (
        <footer style={footerStyle}>
            <div style={footerContainerStyle}>
                {/* Section 1: About Us / Company Info */}
                <div style={sectionStyle}>
                    <h4 style={headingStyle}>E-Commerce App</h4>
                    <p style={textStyle}>
                        Your one-stop shop for all your needs. We are dedicated to providing the best products with fast and reliable service.
                    </p>
                </div>

                {/* Section 2: Quick Links */}
                <div style={sectionStyle}>
                    <h4 style={headingStyle}>Quick Links</h4>
                    <ul style={ulStyle}>
                        <li><Link to="/about" style={linkStyle}>About Us</Link></li>
                        <li><Link to="/contact" style={linkStyle}>Contact</Link></li>
                        <li><Link to="/products" style={linkStyle}>Shop All</Link></li>
                        <li><Link to="/faq" style={linkStyle}>FAQ</Link></li>
                    </ul>
                </div>

                {/* Section 3: Customer Service */}
                <div style={sectionStyle}>
                    <h4 style={headingStyle}>Customer Service</h4>
                    <ul style={ulStyle}>
                        <li><Link to="/shipping" style={linkStyle}>Shipping & Returns</Link></li>
                        <li><Link to="/order-tracking" style={linkStyle}>Order Tracking</Link></li>
                        <li><Link to="/support" style={linkStyle}>Help Center</Link></li>
                    </ul>
                </div>

                {/* Section 4: Social Media & Legal */}
                <div style={sectionStyle}>
                    <h4 style={headingStyle}>Follow Us</h4>
                    <div style={socialIconsStyle}>
                        <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer" style={socialLinkStyle}>
                            <span role="img" aria-label="facebook">📘</span>
                        </a>
                        <a href="https://www.instagram.com" target="_blank" rel="noopener noreferrer" style={socialLinkStyle}>
                            <span role="img" aria-label="instagram">📸</span>
                        </a>
                        <a href="https://www.twitter.com" target="_blank" rel="noopener noreferrer" style={socialLinkStyle}>
                            <span role="img" aria-label="twitter">🐦</span>
                        </a>
                    </div>
                    <ul style={legalUlStyle}>
                        <li><Link to="/privacy-policy" style={legalLinkStyle}>Privacy Policy</Link></li>
                        <li><Link to="/terms-of-service" style={legalLinkStyle}>Terms of Service</Link></li>
                    </ul>
                </div>
            </div>

            <div style={bottomBarContainerStyle}>
                <p style={copyrightStyle}>&copy; {new Date().getFullYear()} E-Commerce App. All Rights Reserved.</p>
            </div>
        </footer>
    );
};

// --- Styles ---

const footerStyle = {
    background: '#222',
    color: '#fff',
    padding: '2rem 0',
    marginTop: 'auto', // Pushes the footer to the bottom of the page
};

const footerContainerStyle = {
    maxWidth: '1200px',
    margin: '0 auto',
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    padding: '0 2rem',
};

const sectionStyle = {
    flex: '1',
    minWidth: '200px',
    marginBottom: '2rem',
};

const headingStyle = {
    fontSize: '1.2rem',
    marginBottom: '1rem',
    borderBottom: '2px solid #007bff',
    paddingBottom: '0.5rem',
};

const ulStyle = {
    listStyle: 'none',
    padding: '0',
};

const linkStyle = {
    color: '#ccc',
    textDecoration: 'none',
    transition: 'color 0.3s ease',
};
linkStyle[':hover'] = {
    color: '#007bff',
};

const textStyle = {
    color: '#ccc',
    lineHeight: '1.6',
};

const socialIconsStyle = {
    display: 'flex',
    gap: '15px',
    marginBottom: '1rem',
};

const socialLinkStyle = {
    fontSize: '1.5rem',
    textDecoration: 'none',
};

const legalUlStyle = {
    listStyle: 'none',
    padding: '0',
    display: 'flex',
    flexDirection: 'column',
    gap: '5px',
};

const legalLinkStyle = {
    color: '#777',
    textDecoration: 'none',
    fontSize: '0.9rem',
};

const bottomBarContainerStyle = {
    borderTop: '1px solid #444',
    marginTop: '2rem',
    paddingTop: '1rem',
    textAlign: 'center',
};

const copyrightStyle = {
    fontSize: '0.8rem',
    color: '#777',
    margin: '0',
};

export default Footer;