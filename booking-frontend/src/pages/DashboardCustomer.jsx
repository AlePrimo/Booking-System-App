import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function DashboardCustomer() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="p-6">
      {/* Header con usuario + logout */}
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-xl font-bold">Panel de Cliente</h1>
        {user && (
          <div className="flex items-center gap-4">
            <span>Bienvenido, {user.name} ({user.email})</span>
            <button
              onClick={logout}
              className="px-3 py-1 border rounded bg-red-500 text-white"
            >
              Logout
            </button>
          </div>
        )}
      </div>

      {/* Menú interno */}
      <div className="flex flex-col gap-4 max-w-xs">
        <button
          onClick={() => navigate("/reservas")}
          className="px-3 py-1 border rounded"
        >
          Reservas
        </button>
        <button
          onClick={() => navigate("/notificaciones")}
          className="px-3 py-1 border rounded"
        >
          Notificaciones
        </button>
        <button
          onClick={() => navigate("/servicios")}
          className="px-3 py-1 border rounded bg-blue-500 text-white"
        >
          Servicios
        </button>
      </div>
    </div>
  );
}
