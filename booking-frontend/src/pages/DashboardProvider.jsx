import { useAuth } from "../context/AuthContext";

export default function DashboardProvider() {
  const { user } = useAuth();

  return (
    <div className="p-6">
      <h1 className="text-xl font-bold">Panel de Proveedor</h1>
      {user && <p>Bienvenido, {user.name} ({user.email})</p>}
      <p>Aquí podrás gestionar tus servicios y revisar las reservas recibidas.</p>
    </div>
  );
}
