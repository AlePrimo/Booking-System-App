// src/components/ProtectedRoute.jsx
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function ProtectedRoute({ children, allowedRoles }) {
  const { user } = useAuth();

  // 🔒 Si no hay usuario -> redirige al login
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // 🔒 Si se pasan roles permitidos y el usuario no tiene ese rol -> unauthorized
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/unauthorized" replace />;
  }

  // ✅ Usuario autorizado -> renderiza el componente hijo
  return children;
}
