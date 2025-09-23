import React, { useContext } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

/**
 * ProtectedRoute
 * Props:
 *  - children: nodo(s) a renderizar si está autorizado
 *  - roles: array de roles aceptados (por ejemplo ["ROLE_ADMIN"] o ["ADMIN"])
 *
 * Comportamiento:
 *  - Si no está autenticado -> redirige a /login (preserva la ubicación original en state.from)
 *  - Si está autenticado pero no tiene el rol requerido -> redirige a /unauthorized (o /)
 *  - Si está autenticado y tiene rol -> renderiza children
 */
export default function ProtectedRoute({ children, roles = [] }) {
  const { user, isAuthenticated } = useContext(AuthContext);
  const location = useLocation();

  // Si no está autenticado -> ir a login
  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Si no se pidieron roles, permitir acceso
  if (!roles || roles.length === 0) {
    return children;
  }

  // Normalizamos roles: quitamos "ROLE_" y comparamos en mayúsculas
  const normalize = (s) => (s || "").toString().toUpperCase().replace(/^ROLE_/, "");

  const userRoles = new Set(((user && user.roles) || []).map(normalize));
  const allowed = roles.map(normalize).some((r) => userRoles.has(r));

  if (!allowed) {
    // si preferís mostrar una página 403 crea /unauthorized y devuelvela aquí
    return <Navigate to="/unauthorized" replace />;
  }

  return children;
}
