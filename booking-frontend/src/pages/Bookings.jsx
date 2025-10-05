import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getBookingsByCustomer } from "../services/api/bookingService";
import { useNavigate } from "react-router-dom";

export default function Bookings() {
  const [bookings, setBookings] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const { user, token } = useAuth();
  const navigate = useNavigate();
  const pageSize = 5; // Cantidad de reservas por página

  const fetchBookings = async (pageNumber = 0) => {
    if (!user || !token) return;

    try {
      const res = await getBookingsByCustomer(user.id, pageNumber, pageSize, token);
      const content = res.data.content || res.data;

      setBookings(content);
      if (res.data.totalPages !== undefined) {
        setTotalPages(res.data.totalPages);
      } else {
        setTotalPages(1);
      }
    } catch (err) {
      console.error("Error al obtener reservas:", err);
    }
  };

  useEffect(() => {
    fetchBookings(page);
  }, [user, token, page]);

  const handleNext = () => {
    if (page + 1 < totalPages) setPage(page + 1);
  };

  const handlePrev = () => {
    if (page > 0) setPage(page - 1);
  };

  return (
    <div className="p-6">
      <button
        onClick={() => navigate("/dashboard-customer")}
        className="px-3 py-1 border rounded mb-4"
      >
        ← Volver al Dashboard
      </button>

      <h2 className="text-2xl font-bold mb-4">Mis Reservas</h2>

      {bookings.length === 0 ? (
        <p>No tienes reservas aún.</p>
      ) : (
        <>
          <ul className="space-y-2 mb-4">
            {bookings.map((b) => (
              <li key={b.id} className="border p-2 rounded">
                <strong>Reserva #{b.id}</strong> - Servicio {b.offeringId} - Estado:{" "}
                {b.status} - Fecha: {new Date(b.bookingDateTime).toLocaleString()}
              </li>
            ))}
          </ul>

          {/* Botones de paginación */}
          <div className="flex justify-between">
            <button
              onClick={handlePrev}
              disabled={page === 0}
              className="px-3 py-1 border rounded disabled:opacity-50"
            >
              Anterior
            </button>
            <span>
              Página {page + 1} de {totalPages}
            </span>
            <button
              onClick={handleNext}
              disabled={page + 1 >= totalPages}
              className="px-3 py-1 border rounded disabled:opacity-50"
            >
              Siguiente
            </button>
          </div>
        </>
      )}
    </div>
  );
}
