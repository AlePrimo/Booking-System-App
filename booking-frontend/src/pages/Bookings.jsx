import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getBookings } from "../api/bookingService";
import { getOfferingById } from "../api/offeringService";
import { getUserById } from "../api/userService"; // Para obtener nombre del proveedor
import { useNavigate } from "react-router-dom";

export default function Bookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user, token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user || !token) return;
    fetchBookings();
  }, [user, token]);

  const fetchBookings = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await getBookings(0, 50, token); // Trae todas las reservas
      const bookingsData = res.data.content || res.data;

      // Filtrar solo reservas del cliente
      const clientBookings = bookingsData.filter(
        (b) => b.customerId === user.id
      );

      // Ordenar por fecha (más próximas primero)
      clientBookings.sort(
        (a, b) =>
          new Date(a.bookingDateTime).getTime() -
          new Date(b.bookingDateTime).getTime()
      );

      // Traer info de offering y proveedor para cada reserva
// Traer info de offering y proveedor para cada reserva
const bookingsWithDetails = await Promise.all(
  clientBookings.map(async (b) => {
    try {
      const offeringRes = await getOfferingById(b.offeringId, token);
      const offering = offeringRes.data;

      let providerName = "Desconocido";

      // 🔹 Detectar correctamente el ID del proveedor
      const providerId =
        offering.providerId ||
        (offering.provider ? offering.provider.id : null);

      if (providerId) {
        const providerRes = await getUserById(providerId, token);
        providerName = providerRes.data.name;
      }

      return {
        ...b,
        offeringName: offering.name,
        offeringDescription: offering.description,
        providerName,
      };
    } catch (err) {
      console.error("Error fetching offering or provider:", err);
      return b;
    }
  })
);


      setBookings(bookingsWithDetails);
    } catch (err) {
      console.error("Error fetching bookings:", err);
      setError("No se pudieron cargar las reservas.");
    } finally {
      setLoading(false);
    }
  };

  // 🔹 Función para devolver color según estado
  const getStatusColor = (status) => {
    switch (status) {
      case "PENDING":
        return "bg-yellow-200 text-yellow-800";
      case "CONFIRMED":
        return "bg-green-200 text-green-800";
      case "CANCELLED":
        return "bg-red-200 text-red-800";
      default:
        return "bg-gray-200 text-gray-800";
    }
  };

  return (
    <div className="p-6 min-h-screen bg-gray-100">
      <button
        onClick={() => navigate("/dashboard-customer")}
        className="px-3 py-1 border rounded mb-6 hover:bg-gray-200 transition"
      >
        ← Volver al Dashboard
      </button>

      <h2 className="text-3xl font-bold mb-6 text-indigo-700">Mis Reservas</h2>

      {loading ? (
        <p className="text-gray-500">Cargando reservas...</p>
      ) : error ? (
        <p className="text-red-500">{error}</p>
      ) : bookings.length === 0 ? (
        <p className="text-gray-500">No tienes reservas aún.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {bookings.map((b) => (
            <div
              key={b.id}
              className="border shadow rounded-lg p-5 hover:shadow-lg transition bg-white flex flex-col justify-between"
            >
              <div>
                <div className="flex justify-between items-center">
                  <h3 className="text-xl font-semibold text-indigo-600">
                    Reserva #{b.id}
                  </h3>
                  <span
                    className={`px-2 py-1 rounded-full text-sm font-medium ${getStatusColor(
                      b.status
                    )}`}
                  >
                    {b.status}
                  </span>
                </div>

                <p className="text-gray-700 mt-2">
                  <strong>Servicio:</strong> {b.offeringName || b.offeringId}
                </p>
                <p className="text-gray-600 mt-1">{b.offeringDescription}</p>
                <p className="text-gray-700 mt-1">
                  <strong>Proveedor:</strong> {b.providerName}
                </p>
              </div>
              <p className="text-gray-500 mt-3 text-sm">
                <strong>Fecha:</strong>{" "}
                {new Date(b.bookingDateTime).toLocaleString()}
              </p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}


