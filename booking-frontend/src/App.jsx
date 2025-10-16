import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";

import Home from "./pages/Home";
import Register from "./pages/Register";

import Bookings from "./pages/Bookings";
import Services from "./pages/customer/Services";
import ServiceDetail from "./pages/customer/ServiceDetail";
import CustomerNotifications from "./pages/customer/CustomerNotifications";
import Users from "./pages/Users";
import Payments from "./pages/customer/Payments";
import ProviderServices from "./pages/provider/ProviderServices";
import DashboardAdmin from "./pages/DashboardAdmin";
import DashboardCustomer from "./pages/customer/DashboardCustomer";
import DashboardProvider from "./pages/provider/DashboardProvider";
import ProviderNotifications from "./pages/provider/ProviderNotifications";
import Unauthorized from "./pages/Unauthorized";
import ProtectedRoute from "./components/ProtectedRoute";
import ProviderReservas from "./pages/provider/ProviderReservas";
import ProviderPagos from "./pages/provider/ProviderPagos";


export default function App() {
  const { isSessionExpired, closeSessionAlert } = useAuth();

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      {/* Banner global de sesión expirada */}
      {isSessionExpired && (
        <div className="bg-red-600 text-white text-center p-4">
          <p>Tu sesión ha expirado. Por favor, inicia sesión nuevamente.</p>
          <button
            onClick={closeSessionAlert}
            className="mt-2 px-3 py-1 border rounded bg-white text-red-600"
          >
            Cerrar
          </button>
        </div>
      )}

      <main className="container mx-auto py-6 px-4 flex-1">
        <Routes>
          {/* Home y Register */}
          <Route path="/" element={<Home />} />
          <Route path="/register" element={<Register />} />

          {/* Dashboards */}
          <Route
            path="/dashboard-admin"
            element={
              <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
                <DashboardAdmin />
              </ProtectedRoute>
            }
          />
          <Route path="/provider/reservas" element={<ProviderReservas />} />
          <Route path="/provider/pagos" element={<ProviderPagos />} />
          <Route
            path="/dashboard-customer"
            element={
              <ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]}>
                <DashboardCustomer />
              </ProtectedRoute>
            }
          />
          <Route
            path="/dashboard-provider"
            element={
              <ProtectedRoute allowedRoles={["ROLE_PROVIDER"]}>
                <DashboardProvider />
              </ProtectedRoute>
            }
          />
<Route path="/provider/servicios" element={<ProviderServices />} />
          {/* Rutas privadas CUSTOMER */}
          <Route
            path="/reservas"
            element={
              <ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]}>
                <Bookings />
              </ProtectedRoute>
            }
          />
          <Route
            path="/notificaciones"
            element={
              <ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]}>
                <CustomerNotifications />
              </ProtectedRoute>
            }
          />
          <Route
            path="/servicios"
            element={
              <ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]}>
                <Services />
              </ProtectedRoute>
            }
          />

          <Route
            path="/provider/notificaciones"
            element={
              <ProtectedRoute allowedRoles={["ROLE_PROVIDER"]}>
                <ProviderNotifications />
              </ProtectedRoute>
            }
          />

          <Route
            path="/servicios/:id"
            element={
              <ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]}>
                <ServiceDetail />
              </ProtectedRoute>
            }
          />

          {/* Rutas privadas ADMIN */}
          <Route
            path="/ofertas"
            element={
              <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
                <Services />
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

          {/* Rutas compartidas */}
          <Route
            path="/payments"
            element={
              <ProtectedRoute allowedRoles={["ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_PROVIDER"]}>
                <Payments />
              </ProtectedRoute>
            }
          />

          {/* Unauthorized */}
          <Route path="/unauthorized" element={<Unauthorized />} />

          {/* Fallback */}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>

      <footer className="bg-gray-800 text-white py-6 text-center">
        <p>&copy; {new Date().getFullYear()} Booking System. Todos los derechos reservados.</p>
      </footer>
    </div>
  );
}

