import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getOfferings } from "../../api/offeringService";
import { createBooking } from "../../api/bookingService";
import { getUserById } from "../../api/userService";
import BookingModal from "../../components/BookingModal";
import ConfirmBookingModal from "../../components/ConfirmBookingModal";
import { useAuth } from "../../context/AuthContext";
import { createNotification } from "../../api/notificationService";

export default function ServiceDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user, token, ensureUser } = useAuth();

  const [service, setService] = useState(null);
  const [providerName, setProviderName] = useState("Desconocido");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showBookingModal, setShowBookingModal] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [bookingData, setBookingData] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const fetchService = async () => {
      try {
        if (!token) throw new Error("Usuario no autenticado");

        const res = await getOfferings(0, 100, token);
        const allServices = res.data.content || res.data;
        const found = allServices.find((s) => s.id === parseInt(id));

        if (!found) {
          setError("Servicio no encontrado");
          return;
        }

        setService(found);

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
  }, [id, token]);

  const handleReserveSubmit = (data) => {
    setBookingData(data);
    setShowBookingModal(false);
    setShowConfirmModal(true);
  };

  const handleConfirmBooking = async () => {
    try {
      if (!token) {
        alert("Usuario no autenticado. Inicia sesión nuevamente.");
        return;
      }

      setSubmitting(true);

      // 🔹 Obtener usuario completo por email
      const currentUser = await ensureUser();

      if (!currentUser || !currentUser.id) {
        alert("No se pudo identificar al usuario. Inicia sesión nuevamente.");
        setSubmitting(false);
        return;
      }

      // Convertir bookingDateTime a formato compatible con LocalDateTime
      let bookingDateTime = new Date(bookingData.bookingDateTime);

      // Formateo "YYYY-MM-DDTHH:mm:ss"
      const pad = (num) => num.toString().padStart(2, "0");
      const formattedDateTime = `${bookingDateTime.getFullYear()}-${pad(
        bookingDateTime.getMonth() + 1
      )}-${pad(bookingDateTime.getDate())}T${pad(bookingDateTime.getHours())}:${pad(
        bookingDateTime.getMinutes()
      )}:${pad(bookingDateTime.getSeconds())}`;

      // 1) Crear la reserva
      await createBooking(
        {
          customerId: currentUser.id,
          offeringId: service.id,
          bookingDateTime: formattedDateTime,
          status: "PENDING",
        },
        token
      );

      // 2) Crear notificaciones explicitamente (frontend) — provider y customer
      // Notificación para el provider
      const notifToProvider = {
        message: `Tienes una nueva reserva de ${currentUser.name} para el servicio ${service.description || service.name} en fecha ${formattedDateTime}`,
        recipientId: service.providerId, // providerId proviene del servicio
        type: "EMAIL",
      };

      // Notificación para el customer (confirmación)
      const notifToCustomer = {
        message: `Has reservado el servicio ${service.description || service.name} con ${providerName} para la fecha ${formattedDateTime}`,
        recipientId: currentUser.id,
        type: "EMAIL",
      };

      // Llamadas separadas; si alguna falla, dejamos que el flujo de reserva siga (pero logueamos)
      try {
        await createNotification(notifToProvider, token);
      } catch (err) {
        console.error("Error creando notificación al provider:", err);
      }

      try {
        await createNotification(notifToCustomer, token);
      } catch (err) {
        console.error("Error creando notificación al customer:", err);
      }

      alert("Reserva creada con éxito");
      setShowConfirmModal(false);
    } catch (err) {
      console.error("Error al crear la reserva:", err);
      alert("No se pudo crear la reserva.");
    } finally {
      setSubmitting(false);
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
          submitting={submitting}
        />
      )}
    </div>
  );
}
