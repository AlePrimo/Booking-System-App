import { useAuth } from "../context/AuthContext";

export default function DashboardAdmin() {
  const { user } = useAuth();

  return (
    <div className="p-6">
      <h1 className="text-xl font-bold">Panel de Administración</h1>
      {user && <p>Bienvenido, {user.name} ({user.email})</p>}
      <p>Aquí podrás gestionar ofertas, usuarios y reservas.</p>
    </div>
  );
}
