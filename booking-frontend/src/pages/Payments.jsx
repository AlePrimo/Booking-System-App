import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getBookingsByCustomer } from "../api/bookingService";
import { getPayments, createPayment } from "../api/paymentService";
import { useNavigate } from "react-router-dom";
import Modal from "react-modal";

Modal.setAppElement("#root");

export default function Payments() {
  const { user, token } = useAuth();
  const navigate = useNavigate();

  const [bookings, setBookings] = useState([]);
  const [selectedBooking, setSelectedBooking] = useState(null);
  const [payments, setPayments] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);

  // Traer reservas del customer logueado
  useEffect(() => {
    if (!user || !token) return;
    fetchBookings();
    fetchPayments();
  }, [user, token]);

  const fetchBookings = async () => {
    try {
      const res = await getBookingsByCustomer(user.id, 0, 50, token);
      setBookings(res.data.content || res.data);
    } catch (err) {
      console.error("Error fetching bookings:", err);
    }
  };

  const fetchPayments = async () => {
    try {
      const res = await getPayments(0, 50, token);
      // Filtrar pagos solo del usuario logueado
      const customerPayments = (res.data.content || res.data).filter(
        (p) => p.booking.customerId === user.id
      );
      setPayments(customerPayments);
    } catch (err) {
      console.error("Error fetching payments:", err);
    }
  };

  const handleBookingSelect = (e) => {
    const booking = bookings.find((b) => b.id === parseInt(e.target.value));
    setSelectedBooking(booking);
  };

  const handleConfirmPayment = async () => {
    if (!selectedBooking) return;
    try {
      await createPayment(
        { bookingId: selectedBooking.id, amount: selectedBooking.price },
        token
      );
      setModalOpen(false);
      fetchPayments();
      alert("Pago realizado correctamente");
    } catch (err) {
      console.error("Error creating payment:", err);
      alert("Error al realizar el pago");
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

      <h2 className="text-3xl font-bold mb-6 text-indigo-700">Pagos</h2>

      {/* Selección de reserva y botón de pago */}
      <div className="bg-white p-6 rounded shadow-md mb-8">
        <label className="block mb-2 font-semibold text-gray-700">
          Selecciona una reserva:
        </label>
        <select
          className="w-full border p-2 rounded mb-4"
          onChange={handleBookingSelect}
          value={selectedBooking?.id || ""}
        >
          <option value="">-- Selecciona --</option>
          {bookings.map((b) => (
            <option key={b.id} value={b.id}>
              {b.serviceName} - {new Date(b.bookingDateTime).toLocaleString()}
            </option>
          ))}
        </select>

        <button
          disabled={!selectedBooking}
          onClick={() => setModalOpen(true)}
          className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition disabled:opacity-50"
        >
          Realizar pago
        </button>
      </div>

      {/* Modal de pago */}
      <Modal
        isOpen={modalOpen}
        onRequestClose={() => setModalOpen(false)}
        className="bg-white p-6 rounded shadow-lg max-w-md mx-auto mt-20"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-start"
      >
        {selectedBooking && (
          <>
            <h3 className="text-xl font-bold mb-4">Confirmar Pago</h3>
            <p>
              <strong>Servicio:</strong> {selectedBooking.serviceName}
            </p>
            <p>
              <strong>Proveedor:</strong> {selectedBooking.providerName}
            </p>
            <p>
              <strong>Fecha:</strong>{" "}
              {new Date(selectedBooking.bookingDateTime).toLocaleString()}
            </p>
            <p>
              <strong>Monto a pagar:</strong> ${selectedBooking.price}
            </p>
            <div className="flex justify-end gap-4 mt-6">
              <button
                onClick={() => setModalOpen(false)}
                className="px-4 py-2 border rounded hover:bg-gray-200 transition"
              >
                Cancelar
              </button>
              <button
                onClick={handleConfirmPayment}
                className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition"
              >
                Confirmar pago
              </button>
            </div>
          </>
        )}
      </Modal>

      {/* Pagos realizados */}
      <h3 className="text-2xl font-semibold mb-4 text-indigo-600">
        Pagos realizados
      </h3>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {payments.map((p) => (
          <div key={p.id} className="border shadow rounded-lg p-4 bg-white">
            <p>
              <strong>Reserva:</strong> {p.booking.serviceName}
            </p>
            <p>
              <strong>Monto:</strong> ${p.amount}
            </p>
            <p>
              <strong>Estado:</strong> {p.status}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}

