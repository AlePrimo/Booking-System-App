import { createContext, useState, useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  // Cargar usuario de localStorage al iniciar
  useEffect(() => {
    const stored = localStorage.getItem("user");
    if (stored) {
      setUser(JSON.parse(stored));
    }
  }, []);

  const login = (userData) => {
    // userData esperado: { username, roles: ["ROLE_USER"], token }
    setUser(userData);
    localStorage.setItem("user", JSON.stringify(userData));

    // Redirigir según rol principal
    if (userData.roles?.includes("ROLE_ADMIN")) {
      navigate("/dashboard-admin");
    } else {
      navigate("/dashboard");
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("user");
    navigate("/login");
  };

  const isAuthenticated = !!user;

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);

