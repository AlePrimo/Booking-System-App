import { createContext, useContext, useState } from "react";
import api from "../api/axiosClient";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null); // estado solo en memoria
  const [token, setToken] = useState(null); // token solo en memoria
  const [refreshToken, setRefreshToken] = useState(null);

  const login = async (email, password) => {
    const res = await api.post("/auth/login", { email, password });

    const t = res.data.token;

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

    return userData;
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    setRefreshToken(null);
  };

  const ensureUser = async () => {
    if (!user || !token) return null;

    const res = await api.get(`/api/users/email/${user.email}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    return res.data;
  };

  return (
    <AuthContext.Provider value={{ user, token, refreshToken, login, logout, ensureUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}


