import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosClient";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/auth/login", { email, password });

      // Guardar tokens
      if (res.data.accessToken) {
        localStorage.setItem("accessToken", res.data.accessToken);
      }
      if (res.data.refreshToken) {
        localStorage.setItem("refreshToken", res.data.refreshToken);
      }

      // Guardar usuario con rol
      const userData = {
        email: res.data.email,
        role: res.data.role, // debe venir del backend: ROLE_ADMIN / ROLE_CUSTOMER / ROLE_PROVIDER
      };

      login(userData);

      // Redirigir según rol
      if (userData.role === "ROLE_ADMIN") {
        navigate("/dashboard-admin");
      } else if (userData.role === "ROLE_PROVIDER") {
        navigate("/dashboard-provider");
      } else {
        navigate("/dashboard-customer");
      }
    } catch (err) {
      console.error("Error en login:", err);
      alert("Email o contraseña incorrectos");
    }
  };

  return (
    <div className="p-6 max-w-sm mx-auto bg-white shadow rounded">
      <h2 className="text-lg font-bold mb-4">Iniciar Sesión</h2>
      <form onSubmit={handleLogin} className="flex flex-col gap-3">
        <input
          type="email"
          placeholder="Email"
          className="border p-2 rounded"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Contraseña"
          className="border p-2 rounded"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit" className="bg-blue-600 text-white py-2 rounded">
          Ingresar
        </button>
      </form>
    </div>
  );
}
