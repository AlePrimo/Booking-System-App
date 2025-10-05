import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getBookingsByCustomer } from "../api/bookingService";
import { getOfferingById } from "../api/offeringService";
import { useNavigate } from "react-router-dom";

export default function Bookings() {
  const [bookings, setBookings] = useState([]);
  const { user, token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user || !token) return;
    fetchBookings();
  }, [user, token]);

  const fetchBookings = async () => {
    try {
      // 🔹 Usamos el endpoint específico del cliente
      const res = await getBookingsByCustomer(user.id, 0, 10, token);
      const bookingsData = res.data.content || res.data;

      const bookingsWithDetails = await Promise.all(
        bookingsData.map(async (b) => {
          try {
            const offeringRes = await getOfferingById(b.offeringId, token);
            const offering = offeringRes.data;
            return {
              ...b,
              offeringName: offering.name,
              offeringDescription: offering.description,
              providerName: offering.provider?.name || "Sin proveedor",
            };
          } catch (err) {
            console.error("Error fetching offering:", err);
            return b;
          }
        })
      );

      setBookings(bookingsWithDetails);
    } catch (err) {
      console.error("Error fetching bookings:", err);
    }
  };

  return (
    <div className="p-6">
      <button
        onClick={() => navigate("/dashboard-customer")}
        className="px-3 py-1 border rounded mb-6 hover:bg-gray-200 transition"
      >
        ← Volver al Dashboard
      </button>

      <h2 className="text-3xl font-bold mb-6 text-indigo-700">Mis Reservas</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {bookings.map((b) => (
          <div
            key={b.id}
            className="border shadow rounded-lg p-4 hover:shadow-lg transition bg-white"
          >
            <h3 className="text-xl font-semibold text-indigo-600">
              Reserva #{b.id} - {b.status}
            </h3>
            <p className="text-gray-700 mt-1">
              <strong>Servicio:</strong> {b.offeringName || b.offeringId}
            </p>
            <p className="text-gray-600">{b.offeringDescription}</p>
            <p className="text-gray-700 mt-1">
              <strong>Proveedor:</strong> {b.providerName || "Desconocido"}
            </p>
            <p className="text-gray-500 mt-1">
              <strong>Fecha:</strong>{" "}
              {new Date(b.bookingDateTime).toLocaleString()}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}
