import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Home() {
  const { user, login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (user) {
      switch (user.role) {
        case "ROLE_CUSTOMER":
          navigate("/dashboard-customer");
          break;
        case "ROLE_ADMIN":
          navigate("/dashboard-admin");
          break;
        case "ROLE_PROVIDER":
          navigate("/dashboard-provider");
          break;
        default:
          navigate("/");
      }
    }
  }, [user, navigate]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await login(email, password);
    } catch (err) {
      setError("Email o contraseña incorrectos");
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-start bg-gray-100 px-4 pt-16">
      {/* Título */}
      <div className="mb-12 text-center">
        <h1 className="text-6xl md:text-7xl font-bold text-indigo-600">
          Bienvenido a Booking System
        </h1>
        <p className="mt-4 text-2xl md:text-3xl text-gray-700">
          Gestiona usuarios, reservas, pagos y más
        </p>
      </div>

      {!user && (
        <div className="w-full max-w-md">
          <div className="bg-white shadow-xl rounded-lg p-8">
            <h2 className="text-3xl font-semibold mb-6 text-center">
              Iniciar Sesión
            </h2>

            <form onSubmit={handleLogin} className="space-y-4">
              <div>
                <label className="block text-gray-700 font-medium mb-1">
                  Email
                </label>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  required
                />
              </div>

              <div>
                <label className="block text-gray-700 font-medium mb-1">
                  Contraseña
                </label>
                <div className="relative">
                  <input
                    type={showPassword ? "text" : "password"}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    required
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700"
                  >
                    {showPassword ? "Ocultar" : "Mostrar"}
                  </button>
                </div>
              </div>

              {error && <p className="text-red-500 text-sm">{error}</p>}

              <button
                type="submit"
                className="w-full py-3 bg-indigo-600 text-white font-semibold rounded-lg hover:bg-indigo-700 transition-colors"
              >
                Iniciar Sesión
              </button>
            </form>

            <p className="mt-6 text-sm text-center text-gray-600">
              ¿No tenés cuenta?{" "}
              <a
                href="/register"
                className="text-indigo-600 font-semibold hover:underline"
              >
                Registrate
              </a>
            </p>
          </div>
        </div>
      )}
    </div>
  );
}
