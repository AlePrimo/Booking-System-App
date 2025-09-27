import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function ProtectedRoute({ children, allowedRoles }) {
  const { user } = useAuth();

  // 1️⃣ Aún no se cargó el usuario del localStorage
  if (user === undefined) return <div className="text-center mt-10">Cargando...</div>;

  // 2️⃣ No está logueado → redirige a login
  if (!user) return <Navigate to="/" replace />; // tu login/home

  // 3️⃣ Verifica roles permitidos
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/unauthorized" replace />;
  }

  // 4️⃣ Todo ok → renderiza componente protegido
  return children;
}
