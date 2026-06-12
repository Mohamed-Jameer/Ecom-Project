// src/hooks/useCart.js
import { useState, useEffect } from "react";

const useCart = () => {
  const [cartCount, setCartCount] = useState(0);

  useEffect(() => {
    const storedCart = JSON.parse(localStorage.getItem("cart") || "[]");
    setCartCount(storedCart.length);

    const handleCartUpdate = () => {
      const updatedCart = JSON.parse(localStorage.getItem("cart") || "[]");
      setCartCount(updatedCart.length);
    };

    window.addEventListener("cartUpdate", handleCartUpdate);
    return () => window.removeEventListener("cartUpdate", handleCartUpdate);
  }, []);

  return { cartCount };
};

export default useCart;
