import { useState } from "react";

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <nav className="bg-white shadow-md">
      <div className="container mx-auto px-6 py-4 flex justify-between items-center">
        {/* Logo */}
        <div className="text-2xl font-bold text-indigo-600">BookingSystem</div>

        {/* Links desktop */}
        <div className="hidden md:flex space-x-6">
          <a href="#" className="hover:text-indigo-600">Home</a>
          <a href="#" className="hover:text-indigo-600">Reservas</a>
          <a href="#" className="hover:text-indigo-600">Login</a>
          <a href="#" className="hover:text-indigo-600">Register</a>
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
          <a href="#" className="block hover:text-indigo-600">Home</a>
          <a href="#" className="block hover:text-indigo-600">Reservas</a>
          <a href="#" className="block hover:text-indigo-600">Login</a>
          <a href="#" className="block hover:text-indigo-600">Register</a>
        </div>
      )}
    </nav>
  );
}
