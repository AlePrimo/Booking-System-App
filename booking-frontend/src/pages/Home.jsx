import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Login from "./Login";

export default function Home() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen flex flex-col items-center bg-gray-100 px-4">
      {/* Título */}
      <div className="mt-12 mb-6 text-center">
        <h1 className="text-5xl font-bold text-indigo-600">
          Bienvenido a Booking System
        </h1>
        <p className="mt-3 text-gray-600 text-lg">
          Gestiona usuarios, reservas, pagos y más.
        </p>
      </div>

      {!user && (
        <div className="w-full max-w-sm">
          {/* Login Card */}
          <div className="bg-white shadow rounded p-6">
            <h2 className="text-2xl font-semibold mb-4 text-center">
              Iniciar Sesión
            </h2>
            <Login />

            {/* Mensaje registro */}
            <p className="mt-4 text-sm text-center text-gray-600">
              ¿No tenés cuenta?{" "}
              <Link
                to="/register"
                className="text-indigo-600 font-semibold hover:underline"
              >
                Registrate
              </Link>
            </p>
          </div>
        </div>
      )}
    </div>
  );
}
