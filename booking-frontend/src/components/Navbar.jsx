import { useState } from "react";
import { Link } from "react-router-dom";
import { Menu, X } from "lucide-react"; // npm install lucide-react

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <nav className="bg-white shadow-md">
      <div className="container mx-auto px-6 py-4 flex justify-between items-center">
        {/* Logo */}
        <Link to="/" className="text-2xl font-bold text-indigo-600">
          BookingSystem
        </Link>

        {/* Links desktop */}
        <div className="hidden md:flex space-x-6">
          <Link to="/" className="hover:text-indigo-600">Home</Link>
          <Link to="/bookings" className="hover:text-indigo-600">Reservas</Link>
          <Link to="/offerings" className="hover:text-indigo-600">Servicios</Link>
          <Link to="/users" className="hover:text-indigo-600">Usuarios</Link>
          <Link to="/payments" className="hover:text-indigo-600">Pagos</Link>
          <Link to="/notifications" className="hover:text-indigo-600">Notificaciones</Link>
          <Link to="/login" className="hover:text-indigo-600">Login</Link>
          <Link to="/register" className="hover:text-indigo-600">Register</Link>
        </div>

        {/* Botón mobile */}
        <button
          onClick={() => setIsOpen(!isOpen)}
          className="md:hidden text-gray-700 focus:outline-none"
        >
          {isOpen ? <X size={24} /> : <Menu size={24} />}
        </button>
      </div>

      {/* Menu mobile */}
      {isOpen && (
        <div className="md:hidden bg-white px-6 py-4 space-y-2">
          <Link to="/" className="block hover:text-indigo-600">Home</Link>
          <Link to="/bookings" className="block hover:text-indigo-600">Reservas</Link>
          <Link to="/offerings" className="block hover:text-indigo-600">Servicios</Link>
          <Link to="/users" className="block hover:text-indigo-600">Usuarios</Link>
          <Link to="/payments" className="block hover:text-indigo-600">Pagos</Link>
          <Link to="/notifications" className="block hover:text-indigo-600">Notificaciones</Link>
          <Link to="/login" className="block hover:text-indigo-600">Login</Link>
          <Link to="/register" className="block hover:text-indigo-600">Register</Link>
        </div>
      )}
    </nav>
  );
}
