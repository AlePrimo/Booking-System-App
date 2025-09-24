import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosClient"; // cliente axios centralizado

export default function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // 👉 login con email y password
      const res = await api.post("/auth/login", { email, password });

      // Guardar tokens
      if (res.data.accessToken) {
        localStorage.setItem("accessToken", res.data.accessToken);
      }
      if (res.data.refreshToken) {
        localStorage.setItem("refreshToken", res.data.refreshToken);
      }
      if (res.data.role) {
        localStorage.setItem("role", res.data.role);
      }

      // 👉 Redirigir según rol
      switch (res.data.role) {
        case "ROLE_ADMIN":
          navigate("/dashboard-admin");
          break;
        case "ROLE_CUSTOMER":
          navigate("/dashboard-customer");
          break;
        case "ROLE_PROVIDER":
          navigate("/dashboard-provider");
          break;
        default:
          navigate("/unauthorized");
      }
    } catch (err) {
      console.error("Error en login:", err);
      alert("Email o contraseña incorrectos");
    }
  };

  return (
    <div className="max-w-md mx-auto bg-white shadow-md rounded-lg p-6">
      <h2 className="text-xl font-bold mb-4">Iniciar Sesión</h2>
      <form onSubmit={handleLogin} className="space-y-4">
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full border rounded px-3 py-2"
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full border rounded px-3 py-2"
        />
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Ingresar
        </button>
      </form>
    </div>
  );
}
