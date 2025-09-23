import { Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";

import Home from "./pages/Home";
import Bookings from "./pages/Bookings";
import Offerings from "./pages/Offerings";
import Users from "./pages/Users";
import Payments from "./pages/Payments";
import Notifications from "./pages/Notifications";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import DashboardAdmin from "./pages/DashboardAdmin";
import Unauthorized from "./pages/Unauthorized";

import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <main className="container mx-auto py-6 px-4">
        <Routes>
          {/* Página pública */}
          <Route path="/" element={<Home />} />

          {/* Auth */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Dashboards */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute allowedRoles={["ROLE_USER"]}>
                <Dashboard />
              </ProtectedRoute>
            }
          />

          <Route
            path="/dashboard-admin"
            element={
              <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
                <DashboardAdmin />
              </ProtectedRoute>
            }
          />

          {/* Rutas privadas USER */}
          <Route
            path="/reservas"
            element={
              <ProtectedRoute allowedRoles={["ROLE_USER"]}>
                <Bookings />
              </ProtectedRoute>
            }
          />

          <Route
            path="/notificaciones"
            element={
              <ProtectedRoute allowedRoles={["ROLE_USER"]}>
                <Notifications />
              </ProtectedRoute>
            }
          />

          {/* Rutas privadas ADMIN */}
          <Route
            path="/ofertas"
            element={
              <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
                <Offerings />
              </ProtectedRoute>
            }
          />

          <Route
            path="/usuarios"
            element={
              <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
                <Users />
              </ProtectedRoute>
            }
          />

          {/* Compartido (ejemplo) */}
          <Route
            path="/payments"
            element={
              <ProtectedRoute allowedRoles={["ROLE_USER", "ROLE_ADMIN"]}>
                <Payments />
              </ProtectedRoute>
            }
          />

          {/* Unauthorized */}
          <Route path="/unauthorized" element={<Unauthorized />} />

          {/* Fallback: cualquier ruta inválida redirige a Home */}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>

      <footer className="bg-gray-800 text-white py-6 text-center">
        <p>&copy; {new Date().getFullYear()} Booking System. Todos los derechos reservados.</p>
      </footer>
    </div>
  );
}

export default App;
