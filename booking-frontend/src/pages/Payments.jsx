import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getBookings } from "../api/bookingService";
import { getOfferingById } from "../api/offeringService";
import { getUserById } from "../api/userService";

Modal.setAppElement("#root");

const Payments = () => {
  const navigate = useNavigate();
  const { user, token } = useAuth();

  const [bookings, setBookings] = useState([]);
  const [selectedBookingId, setSelectedBookingId] = useState("");
  const [selectedBooking, setSelectedBooking] = useState(null);
  const [loading, setLoading] = useState(true);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState("credit-card");
  const [cardNumber, setCardNumber] = useState("");

  // 🔹 Cargar reservas del usuario logueado
  useEffect(() => {
    if (!user || !token) return;
    fetchBookings();
  }, [user, token]);

  const fetchBookings = async () => {
    setLoading(true);
    try {
      const res = await getBookings(0, 50, token);
      const data = res.data.content || res.data;
      const clientBookings = data.filter((b) => b.customerId === user.id);

      const enriched = await Promise.all(
        clientBookings.map(async (b) => {
          try {
            const offeringRes = await getOfferingById(b.offeringId, token);
            const offering = offeringRes.data;

            let providerName = "Desconocido";
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
              offeringPrice: offering.price,
              providerName,
            };
          } catch {
            return b;
          }
        })
      );

      setBookings(enriched);
    } catch (err) {
      console.error("Error al traer reservas:", err);
    } finally {
      setLoading(false);
    }
  };

  // 🔹 Abrir modal con la reserva seleccionada
  const handlePayment = () => {
    if (!selectedBookingId) {
      alert("Seleccioná una reserva antes de continuar");
      return;
    }
    const booking = bookings.find((b) => b.id === Number(selectedBookingId));
    setSelectedBooking(booking);
    setIsModalOpen(true);
  };

  // 🔹 Confirmar pago (simulado)
  const handleConfirmPayment = () => {
    alert(`Pago confirmado para la reserva #${selectedBooking.id}`);
    setIsModalOpen(false);
    setSelectedBookingId("");
    setCardNumber("");
  };

  return (
    <div className="p-6 min-h-screen bg-gray-100 flex flex-col items-center">
      {/* Botón Volver */}
      <div className="self-start mb-4">
        <button
          onClick={() => navigate("/dashboard-customer")}
          className="px-4 py-2 border border-gray-300 rounded-lg bg-white hover:bg-gray-200 transition"
        >
          ← Volver al Dashboard
        </button>
      </div>

      <div className="bg-white shadow-lg rounded-2xl p-8 w-full max-w-lg">
        <h1 className="text-3xl font-bold mb-6 text-indigo-700 text-center">
          Pagos
        </h1>

        {loading ? (
          <p className="text-gray-500 text-center">Cargando reservas...</p>
        ) : bookings.length === 0 ? (
          <p className="text-gray-500 text-center">
            No tenés reservas disponibles para pagar.
          </p>
        ) : (
          <>
            <div className="mb-6">
              <label
                htmlFor="booking-select"
                className="block text-gray-700 font-medium mb-2"
              >
                Seleccioná una reserva para pagar
              </label>
              <select
                id="booking-select"
                value={selectedBookingId}
                onChange={(e) => setSelectedBookingId(e.target.value)}
                className="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-indigo-400 focus:outline-none"
              >
                <option value="">-- Seleccionar --</option>
                {bookings.map((b) => (
                  <option key={b.id} value={b.id}>
                    {`Reserva #${b.id} - ${b.offeringName} (${b.providerName})`}
                  </option>
                ))}
              </select>
            </div>

            <div className="flex flex-col gap-4">
              <button
                onClick={handlePayment}
                className="bg-indigo-600 text-white py-2 px-4 rounded-lg hover:bg-indigo-700 transition"
              >
                Realizar Pago
              </button>
            </div>
          </>
        )}
      </div>

      {/* 🔹 Modal de pago */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        className="bg-white rounded-2xl shadow-2xl p-6 w-full max-w-md mx-auto mt-24 outline-none"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
      >
        {selectedBooking && (
          <div>
            <h2 className="text-2xl font-semibold mb-4 text-indigo-700 text-center">
              Confirmar Pago
            </h2>

            <p className="text-gray-700 mb-2">
              <strong>Servicio:</strong> {selectedBooking.offeringName}
            </p>
            <p className="text-gray-700 mb-2">
              <strong>Proveedor:</strong> {selectedBooking.providerName}
            </p>
            <p className="text-gray-700 mb-2">
              <strong>Fecha:</strong>{" "}
              {new Date(selectedBooking.bookingDateTime).toLocaleString()}
            </p>
            <p className="text-gray-700 mb-4">
              <strong>Monto:</strong> ${selectedBooking.offeringPrice}
            </p>

            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-1">
                Método de pago
              </label>
              <select
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
                className="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-indigo-400 focus:outline-none"
              >
                <option value="credit-card">Tarjeta de crédito</option>
                <option value="debit-card">Tarjeta de débito</option>
                <option value="transfer">Transferencia bancaria</option>
              </select>
            </div>

            {(paymentMethod === "credit-card" ||
              paymentMethod === "debit-card") && (
              <div className="mb-4">
                <label className="block text-gray-700 font-medium mb-1">
                  Número de tarjeta
                </label>
                <input
                  type="text"
                  value={cardNumber}
                  onChange={(e) => setCardNumber(e.target.value)}
                  placeholder="1234 5678 9012 3456"
                  className="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-indigo-400 focus:outline-none"
                />
              </div>
            )}

            <div className="flex justify-between mt-6">
              <button
                onClick={() => setIsModalOpen(false)}
                className="bg-gray-300 text-gray-800 py-2 px-4 rounded-lg hover:bg-gray-400 transition"
              >
                Cancelar
              </button>
              <button
                onClick={handleConfirmPayment}
                className="bg-indigo-600 text-white py-2 px-4 rounded-lg hover:bg-indigo-700 transition"
              >
                Confirmar Pago
              </button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default Payments;




