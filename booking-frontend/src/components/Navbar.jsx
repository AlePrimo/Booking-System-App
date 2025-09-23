import { useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const { user, logout } = useAuth();

  const hasRole = (role) => user?.roles?.includes(role);

  return (
    <nav className="bg-white shadow-md">
      <div className="container mx-auto px-6 py-4 flex justify-between items-center">
        {/* Logo */}
        <Link to="/" className="text-2xl font-bold text-indigo-600">
          BookingSystem
        </Link>

        {/* Links desktop */}
        <div className="hidden md:flex space-x-6">
          {!user && (
            <>
              <Link to="/login" className="hover:text-indigo-600">Login</Link>
              <Link to="/register" className="hover:text-indigo-600">Register</Link>
            </>
          )}

          {hasRole("ROLE_USER") && (
            <>
              <Link to="/reservas" className="hover:text-indigo-600">Reservas</Link>
              <Link to="/notificaciones" className="hover:text-indigo-600">Notificaciones</Link>
            </>
          )}

          {hasRole("ROLE_ADMIN") && (
            <>
              <Link to="/ofertas" className="hover:text-indigo-600">Ofertas</Link>
              <Link to="/usuarios" className="hover:text-indigo-600">Usuarios</Link>
            </>
          )}

          {user && (
            <button
              onClick={logout}
              className="hover:text-red-600 font-semibold"
            >
              Logout
            </button>
          )}
        </div>

        {/* Botón mobile */}
        <button
          onClick={() => setIsOpen(!isOpen)}
          className="md:hidden text-gray-700 focus:outline-none"
        >
          ☰
        </button>
      </div>

      {/* Menu mobile */}
      {isOpen && (
        <div className="md:hidden bg-white px-6 py-4 space-y-2">
          {!user && (
            <>
              <Link to="/login" className="block hover:text-indigo-600">Login</Link>
              <Link to="/register" className="block hover:text-indigo-600">Register</Link>
            </>
          )}

          {hasRole("ROLE_USER") && (
            <>
              <Link to="/reservas" className="block hover:text-indigo-600">Reservas</Link>
              <Link to="/notificaciones" className="block hover:text-indigo-600">Notificaciones</Link>
            </>
          )}

          {hasRole("ROLE_ADMIN") && (
            <>
              <Link to="/ofertas" className="block hover:text-indigo-600">Ofertas</Link>
              <Link to="/usuarios" className="block hover:text-indigo-600">Usuarios</Link>
            </>
          )}

          {user && (
            <button
              onClick={logout}
              className="block hover:text-red-600 font-semibold"
            >
              Logout
            </button>
          )}
        </div>
      )}
    </nav>
  );
}
