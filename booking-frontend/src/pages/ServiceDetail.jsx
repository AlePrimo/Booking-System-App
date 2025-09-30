import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getOfferings } from "../api/offeringService";
import BookingModal from "../components/BookingModal";

export default function ServiceDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [service, setService] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showBookingModal, setShowBookingModal] = useState(false);

  useEffect(() => {
    const fetchService = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const res = await getOfferings(0, 10, token);
        const allServices = res.data.content || res.data;
        const found = allServices.find((s) => s.id === parseInt(id));
        setService(found);
      } catch (err) {
        setError("No se pudo cargar el servicio.");
      } finally {
        setLoading(false);
      }
    };
    fetchService();
  }, [id]);

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
      <p className="mb-4">Proveedor: {service.providerName || service.provider?.name}</p>

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
        />
      )}
    </div>
  );
}
