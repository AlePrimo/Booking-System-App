import { createContext, useContext, useState, useEffect } from "react";
import api from "../api/axiosClient";
import jwtDecode from "jwt-decode"; // ✅ sin llaves

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [refreshToken, setRefreshToken] = useState(null);
  const [isSessionExpired, setIsSessionExpired] = useState(false);
  const [tokenExpiryTime, setTokenExpiryTime] = useState(null);

  // ✅ Restaurar token guardado en localStorage
  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    if (storedToken) {
      setToken(storedToken);
      try {
        const decoded = jwtDecode(storedToken);
        setTokenExpiryTime(decoded.exp);
      } catch (err) {
        console.error("Error decodificando token:", err);
      }
    }
  }, []);

  // 🔹 Verifica expiración
  useEffect(() => {
    if (!token || !tokenExpiryTime) return;
    const now = Date.now();
    const timeout = tokenExpiryTime * 1000 - now;
    if (timeout <= 0) return handleSessionExpired();
    const timer = setTimeout(handleSessionExpired, timeout);
    return () => clearTimeout(timer);
  }, [token, tokenExpiryTime]);

  const handleSessionExpired = () => {
    setUser(null);
    setToken(null);
    setRefreshToken(null);
    setTokenExpiryTime(null);
    setIsSessionExpired(true);
    localStorage.removeItem("token"); // ✅ limpia también localStorage
  };

const login = async (email, password) => {
  const res = await api.post("/auth/login", { email, password });

  const t = res.data.token;
  const decoded = jwtDecode(t);
  setTokenExpiryTime(decoded.exp);

  // 🔹 Guardar el token en localStorage para que axios lo use
  localStorage.setItem("accessToken", t);
  if (res.data.refreshToken) {
    localStorage.setItem("refreshToken", res.data.refreshToken);
  }

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
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
};


  const closeSessionAlert = () => setIsSessionExpired(false);

  const ensureUser = async () => {
    if (!user || !token) return null;
    try {
      const res = await api.get(`/api/users/email/${user.email}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return res.data;
    } catch (err) {
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

