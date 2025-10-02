import { createContext, useContext, useState, useEffect } from "react";
import api from "../api/axiosClient";
import jwtDecode from "jwt-decode"; // npm install jwt-decode

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [refreshToken, setRefreshToken] = useState(null);
  const [isSessionExpired, setIsSessionExpired] = useState(false);
  const [tokenExpiryTime, setTokenExpiryTime] = useState(null);

  // 🔹 Check automático de expiración
  useEffect(() => {
    if (!token || !tokenExpiryTime) return;

    const now = Date.now();
    const timeout = tokenExpiryTime * 1000 - now;

    if (timeout <= 0) {
      handleSessionExpired();
      return;
    }

    const timer = setTimeout(() => {
      handleSessionExpired();
    }, timeout);

    return () => clearTimeout(timer);
  }, [token, tokenExpiryTime]);

  const handleSessionExpired = () => {
    setUser(null);
    setToken(null);
    setRefreshToken(null);
    setTokenExpiryTime(null);
    setIsSessionExpired(true);
  };

  const login = async (email, password) => {
    const res = await api.post("/auth/login", { email, password });

    const t = res.data.token;
    const decoded = jwtDecode(t);
    setTokenExpiryTime(decoded.exp); // guarda timestamp de expiración

    // Obtener usuario completo por email
    const userRes = await api.get(`/api/users/email/${email}`, {
      headers: { Authorization: `Bearer ${t}` },
    });

    const userData = {
      id: userRes.data.id,
      name: userRes.data.name,
      email: email,
      role: res.data.role,
    };

    setUser(userData);
    setToken(t);
    setRefreshToken(res.data.refreshToken);
    setIsSessionExpired(false);

    return userData;
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    setRefreshToken(null);
    setTokenExpiryTime(null);
    setIsSessionExpired(false);
  };

  const closeSessionAlert = () => {
    setIsSessionExpired(false);
  };

  const ensureUser = async () => {
    if (!user || !token) return null;

    try {
      const res = await api.get(`/api/users/email/${user.email}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return res.data;
    } catch (err) {
      // Si devuelve 401/403, la sesión expiró
      handleSessionExpired();
      return null;
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        refreshToken,
        login,
        logout,
        ensureUser,
        isSessionExpired,
        closeSessionAlert,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

