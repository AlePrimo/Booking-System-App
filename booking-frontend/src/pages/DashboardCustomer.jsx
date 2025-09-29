import { useAuth } from "../context/AuthContext";
import Offerings from "./Offerings";

export default function DashboardCustomer() {
  const { user } = useAuth();

  return (
    <div className="p-6 space-y-6">
      <div>
        <h1 className="text-xl font-bold">Panel de Cliente</h1>
        {user && (
          <p>
            Bienvenido, {user.name} ({user.email})
          </p>
        )}
        <p>Aquí podrás ver tus reservas, notificaciones y perfil.</p>
      </div>

      <div>
        <Offerings />
      </div>
    </div>
  );
}
