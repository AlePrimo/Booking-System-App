import { createContext, useContext, useState, useEffect } from "react";
import api from "../api/axiosClient";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(undefined); // undefined = aún no cargado

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    } else {
      setUser(null);
    }
  }, []);

  // 🔹 Login real que llama al backend
  const login = async (email, password) => {
    const res = await api.post("/auth/login", { email, password });

    // Backend devuelve: id + token + refreshToken + role (+ name opcional)
    const userData = {
      id: res.data.id,       // <-- ID real del usuario (Long en el back)
      name: res.data.name,   // opcional, depende de lo que devuelva tu back
      email: email,          // lo guardamos solo como referencia
      role: res.data.role,   // ROLE_CUSTOMER o ROLE_PROVIDER
    };

    setUser(userData);
    localStorage.setItem("user", JSON.stringify(userData));
    localStorage.setItem("accessToken", res.data.token);
    localStorage.setItem("refreshToken", res.data.refreshToken);

    return userData;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("user");
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  };

  // 🔹 Si alguna vez necesitás refrescar datos del usuario desde el back:
  const ensureUserId = async () => {
    if (!user) return null;

    const token = localStorage.getItem("accessToken");
    // 🚀 ahora usamos el ID, no el email
    const res = await api.get(`/api/users/${user.id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    return res.data;
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, ensureUserId }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

