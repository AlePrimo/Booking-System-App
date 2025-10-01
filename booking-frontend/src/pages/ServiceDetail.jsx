import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getOfferings } from "../api/offeringService";
import { createBooking } from "../api/bookingService";
import { getUserById } from "../api/userService";
import BookingModal from "../components/BookingModal";
import ConfirmBookingModal from "../components/ConfirmBookingModal";
import { useAuth } from "../context/AuthContext";

export default function ServiceDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [service, setService] = useState(null);
  const [providerName, setProviderName] = useState("Desconocido");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showBookingModal, setShowBookingModal] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [bookingData, setBookingData] = useState(null);

  useEffect(() => {
    const fetchService = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const res = await getOfferings(0, 100, token); // traemos todos los servicios
        const allServices = res.data.content || res.data;
        const found = allServices.find((s) => s.id === parseInt(id));
        if (!found) {
          setError("Servicio no encontrado");
          return;
        }
        setService(found);

        // Traer el nombre del proveedor
        if (found.providerId) {
          try {
            const providerRes = await getUserById(found.providerId, token);
            setProviderName(providerRes.data.name || "Desconocido");
          } catch {
            setProviderName("Desconocido");
          }
        }
      } catch (err) {
        console.error(err);
        setError("No se pudo cargar el servicio.");
      } finally {
        setLoading(false);
      }
    };

    fetchService();
  }, [id]);

  const handleReserveSubmit = (data) => {
    setBookingData(data);
    setShowBookingModal(false);
    setShowConfirmModal(true);
  };

  const handleConfirmBooking = async () => {
    if (!service || !bookingData) return;

    try {
      const token = localStorage.getItem("accessToken");
      await createBooking(
        {
          customerId: user.id,
          offeringId: service.id,
          bookingDateTime: bookingData.bookingDateTime,
          status: "PENDING",
        },
        token
      );
      alert("Reserva creada con éxito");
      setShowConfirmModal(false);
    } catch (err) {
      console.error("Error al crear la reserva:", err);
      alert("No se pudo crear la reserva.");
    }
  };

  if (loading) return <p className="p-6">Cargando servicio...</p>;
  if (error) return <p className="p-6 text-red-500">{error}</p>;
  if (!service) return <p className="p-6">Servicio no encontrado</p>;

  return (
    <div className="p-6">
      <button
        onClick={() => navigate(-1)}
        className="px-3 py-1 border rounded mb-4"
      >
        ← Volver
      </button>

      <h2 className="text-2xl font-bold mb-2">{service.name}</h2>
      <p className="mb-2">{service.description || "Sin descripción"}</p>
      <p className="mb-2">Precio: ${service.price}</p>
      <p className="mb-4">Proveedor: {providerName}</p>

      <button
        onClick={() => setShowBookingModal(true)}
        className="px-4 py-2 bg-indigo-600 text-white rounded"
      >
        Reservar
      </button>

      {showBookingModal && (
        <BookingModal
          service={service}
          onClose={() => setShowBookingModal(false)}
          onSubmit={handleReserveSubmit}
        />
      )}

      {showConfirmModal && bookingData && (
        <ConfirmBookingModal
          service={service}
          bookingData={bookingData}
          onConfirm={handleConfirmBooking}
          onCancel={() => setShowConfirmModal(false)}
        />
      )}
    </div>
  );
}
