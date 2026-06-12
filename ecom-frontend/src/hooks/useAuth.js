// src/hooks/useAuth.js
import { useState, useEffect } from "react";

const useAuth = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [role, setRole] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("jwtToken");
    const storedRole = localStorage.getItem("role");
    setIsLoggedIn(!!token);
    setRole(storedRole || null);

    // Listen to logout event from Header
    const handleLogoutEvent = () => setIsLoggedIn(false);
    window.addEventListener("logout", handleLogoutEvent);
    return () => window.removeEventListener("logout", handleLogoutEvent);
  }, []);

  return { isLoggedIn, role };
};

export default useAuth;
