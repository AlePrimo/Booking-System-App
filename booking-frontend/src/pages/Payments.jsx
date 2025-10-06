import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getBookingsByCustomer } from "../api/bookingService";
import { getPayments, createPayment } from "../api/paymentService";

export default function Payments() {
  const [payments, setPayments] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [bookingId, setBookingId] = useState("");
  const [amount, setAmount] = useState("");
  const { user, token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user || !token) return;
    fetchPayments();
    fetchBookings();
  }, [user, token]);

  const fetchPayments = async () => {
    try {
      const res = await getPayments(0, 50, token); // Trae todas
      const filtered = (res.data.content || res.data).filter(
        (p) => p.customerId === user.id
      );
      setPayments(filtered);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchBookings = async () => {
    try {
      const res = await getBookingsByCustomer(user.id, token);
      setBookings(res.data || []);
    } catch (err) {
      console.error(err);
    }
  };

  const handleCreatePayment = async (e) => {
    e.preventDefault();
    try {
      await createPayment({ bookingId: Number(bookingId), amount: Number(amount) }, token);
      setBookingId("");
      setAmount("");
      fetchPayments();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6">
      <button onClick={() => navigate("/dashboard-customer")} className="px-4 py-2 mb-6 rounded bg-gray-200 hover:bg-gray-300 transition">
        ← Volver al Dashboard
      </button>

      <h2 className="text-3xl font-bold mb-6 text-indigo-600">Pagos</h2>

      {/* Formulario */}
      <form onSubmit={handleCreatePayment} className="mb-8 bg-white shadow-md p-6 rounded-lg grid grid-cols-1 md:grid-cols-3 gap-4">
        <select value={bookingId} onChange={(e) => setBookingId(e.target.value)} className="border p-2 rounded w-full" required>
          <option value="">Selecciona una reserva</option>
          {bookings.map((b) => (
            <option key={b.id} value={b.id}>
              Reserva #{b.id} - {b.offeringName}
            </option>
          ))}
        </select>
        <input type="number" placeholder="Monto" value={amount} onChange={(e) => setAmount(e.target.value)} className="border p-2 rounded w-full" required />
        <button type="submit" className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition w-full">
          Realizar pago
        </button>
      </form>

      {/* Listado */}
      {payments.length === 0 ? (
        <p className="text-gray-500">No tienes pagos registrados.</p>
      ) : (
        <ul className="space-y-4">
          {payments.map((p) => (
            <li key={p.id} className="p-4 rounded-lg shadow-md bg-white flex justify-between items-center">
              <div>
                <p><span className="font-semibold">Pago #{p.id}</span> - Reserva #{p.bookingId}</p>
                <p className="text-gray-600">Monto: ${p.amount}</p>
              </div>
              <div className="text-sm text-gray-500">{p.status || "Pendiente"}</div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

