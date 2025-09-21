import { useState } from "react";
import Navbar from "./components/Navbar";

function App() {
  return (
    <div className="min-h-screen bg-gray-100">
      {/* Navbar */}
      <Navbar />

      {/* Hero Section */}
      <header className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white py-20">
        <div className="container mx-auto px-6 text-center">
          <h1 className="text-5xl font-bold mb-4">
            Bienvenido a Booking System
          </h1>
          <p className="text-lg mb-6">
            Gestiona reservas, pagos y usuarios de manera sencilla.
          </p>
          <button className="bg-white text-indigo-600 px-6 py-3 rounded-lg font-semibold hover:bg-gray-200 transition">
            Empezar ahora
          </button>
        </div>
      </header>

      {/* Contenido */}
      <main className="container mx-auto px-6 py-12">
        <h2 className="text-2xl font-bold mb-4">Próximas características</h2>
        <ul className="list-disc list-inside space-y-2">
          <li>Gestión de reservas en línea</li>
          <li>Pagos seguros con integración</li>
          <li>Panel de administración</li>
        </ul>
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white py-6 text-center">
        <p>&copy; {new Date().getFullYear()} Booking System. Todos los derechos reservados.</p>
      </footer>
    </div>
  );
}

export default App;

