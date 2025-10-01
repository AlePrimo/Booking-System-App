import { createContext, useContext, useState, useEffect } from "react";
import api from "../api/axiosClient";
import { getUserById } from "../api/userService"; // 🔹 para buscar datos completos

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(undefined);

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    } else {
      setUser(null);
    }
  }, []);

  const login = async (email, password) => {
    const res = await api.post("/auth/login", { email, password });

    const userData = {
      name: res.data.name,
      email: email,
      role: res.data.role,
      id: res.data.id || null, // si no viene, lo buscamos después
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

  // 🔹 Método auxiliar: si el user no tiene id, lo buscamos y actualizamos
  const ensureUserId = async () => {
    if (user && !user.id) {
      const token = localStorage.getItem("accessToken");
      try {
        const res = await getUserById(user.email, token);
        const updated = { ...user, id: res.data.id };
        setUser(updated);
        localStorage.setItem("user", JSON.stringify(updated));
        return updated;
      } catch (err) {
        console.error("No se pudo obtener el id del usuario:", err);
      }
    }
    return user;
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
