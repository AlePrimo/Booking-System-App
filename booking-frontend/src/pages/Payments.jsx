import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getPayments, createPayment } from "../api/paymentService";
import { getBookings } from "../api/bookingService";
import { useNavigate } from "react-router-dom";

export default function Payments() {
  const [payments, setPayments] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [selectedBooking, setSelectedBooking] = useState("");
  const [amount, setAmount] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const { token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) return;
    fetchPayments();
    fetchBookings();
  }, [token]);

  const fetchPayments = async () => {
    try {
      const res = await getPayments(0, 20, token);
      setPayments(res.data.content || res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchBookings = async () => {
    try {
      const res = await getBookings(0, 50, token);
      setBookings(res.data.content || res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleCreatePayment = async (e) => {
    e.preventDefault();
    if (!selectedBooking || !amount) return;

    setLoading(true);
    try {
      await createPayment(
        { bookingId: selectedBooking, amount: parseFloat(amount) },
        token
      );
      setMessage("Pago realizado con éxito");
      setSelectedBooking("");
      setAmount("");
      fetchPayments();
    } catch (err) {
      console.error(err);
      setMessage("Error al realizar el pago");
    } finally {
      setLoading(false);
      setTimeout(() => setMessage(""), 3000);
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

      {/* FORMULARIO DE NUEVO PAGO */}
      <div className="bg-white shadow rounded-lg p-6 mb-6">
        <h3 className="text-xl font-semibold mb-4">Realizar un pago</h3>
        {message && (
          <div className="mb-4 text-green-600 font-medium">{message}</div>
        )}
        <form onSubmit={handleCreatePayment} className="flex flex-col gap-4">
          <select
            className="border rounded px-3 py-2"
            value={selectedBooking}
            onChange={(e) => setSelectedBooking(e.target.value)}
          >
            <option value="">Selecciona una reserva</option>
            {bookings.map((b) => (
              <option key={b.id} value={b.id}>
                Reserva #{b.id} - {b.status}
              </option>
            ))}
          </select>
          <input
            type="number"
            placeholder="Monto"
            className="border rounded px-3 py-2"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            min="0"
          />
          <button
            type="submit"
            className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 transition"
            disabled={loading}
          >
            {loading ? "Procesando..." : "Pagar"}
          </button>
        </form>
      </div>

      {/* LISTADO DE PAGOS */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {payments.map((p) => (
          <div
            key={p.id}
            className="border shadow rounded-lg p-4 hover:shadow-lg transition bg-white"
          >
            <h3 className="text-xl font-semibold text-indigo-600">
              Pago #{p.id}
            </h3>
            <p className="text-gray-700 mt-1">
              <strong>Reserva:</strong> {p.bookingId}
            </p>
            <p className="text-gray-700 mt-1">
              <strong>Monto:</strong> ${p.amount}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}

