import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosClient";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
    setErrorMsg(null);
    setLoading(true);
    console.log("handleLogin ejecutado", { email });

    try {
      const res = await api.post("/auth/login", { email, password });
      console.log("Respuesta backend:", res.data);

      // Guardar tokens si vienen
      if (res.data.accessToken) {
        localStorage.setItem("accessToken", res.data.accessToken);
        console.log("accessToken guardado");
      }
      if (res.data.refreshToken) {
        localStorage.setItem("refreshToken", res.data.refreshToken);
        console.log("refreshToken guardado");
      } else {
        console.log("No hay refreshToken en la respuesta, se omite");
      }

      // Preparar datos del usuario
      const userData = {
        id: res.data.id ?? null,
        name: res.data.name ?? null,
        email: res.data.email ?? email,
        role: res.data.role ?? null,
      };
      console.log("Datos del usuario para contexto:", userData);

      // Actualizar contexto (AuthContext)
      login(userData);
      console.log("login(userData) ejecutado");

      // Redirigir según rol
      if (!userData.role) {
        console.warn("No se recibió role del backend - redirigiendo al home");
        navigate("/");
      } else {
        switch (userData.role) {
          case "ROLE_ADMIN":
            navigate("/dashboard-admin");
            break;
          case "ROLE_PROVIDER":
            navigate("/dashboard-provider");
            break;
          case "ROLE_CUSTOMER":
            navigate("/dashboard-customer");
            break;
          default:
            console.warn("Rol desconocido:", userData.role, "- redirigiendo al home");
            navigate("/");
        }
      }
    } catch (err) {
      console.error("Error en login:", err);

      // Manejo de errores para dar feedback al usuario
      const status = err?.response?.status;
      if (status === 401) {
        setErrorMsg("Credenciales inválidas. Verificá email y contraseña.");
      } else if (status === 403) {
        setErrorMsg("Acceso denegado. Contactá al administrador.");
      } else if (err?.message) {
        setErrorMsg("Error en la autenticación: " + err.message);
      } else {
        setErrorMsg("Error en la autenticación. Revisá la consola.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-sm mx-auto bg-white shadow rounded">
      <h2 className="text-lg font-bold mb-4">Iniciar Sesión</h2>

      {errorMsg && (
        <div className="bg-red-100 text-red-800 p-2 rounded mb-3">
          {errorMsg}
        </div>
      )}

      <form onSubmit={handleLogin} className="flex flex-col gap-3">
        <input
          type="email"
          placeholder="Email"
          className="border p-2 rounded"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <div className="relative">
          <input
            type={showPassword ? "text" : "password"}
            placeholder="Contraseña"
            className="border p-2 rounded w-full pr-10"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button
            type="button"
            onClick={() => setShowPassword((s) => !s)}
            className="absolute right-2 top-1/2 -translate-y-1/2 text-gray-600 text-sm"
            aria-label={showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
          >
            {showPassword ? "🙈" : "👁️"}
          </button>
        </div>

        <button
          type="submit"
          className={`bg-blue-600 text-white py-2 rounded ${loading ? "opacity-70 cursor-wait" : ""}`}
          disabled={loading}
        >
          {loading ? "Ingresando..." : "Ingresar"}
        </button>
      </form>

      <p className="mt-3 text-sm text-gray-500">
        ¿No tenés cuenta? <button onClick={() => navigate("/register")} className="text-blue-600 underline">Registrate</button>
      </p>
    </div>
  );
}
