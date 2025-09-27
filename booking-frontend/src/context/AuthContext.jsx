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

    // Backend devuelve token + refreshToken + role
    const userData = {
      name: res.data.name, // si tu backend devuelve name
      email: email,
      role: res.data.role, // ROLE_CUSTOMER o ROLE_PROVIDER
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

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

