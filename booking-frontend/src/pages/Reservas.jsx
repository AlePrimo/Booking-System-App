import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import {
  getBookings,
  createBooking,
  deleteBooking,
} from "../api/bookingService";

export default function Reservas() {
  const { token } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [title, setTitle] = useState("");

  useEffect(() => {
    if (token) {
      getBookings(0, 10, token).then((res) => setBookings(res.data.content));
    }
  }, [token]);

  const handleCreate = async () => {
    const newBooking = { title }; // 👈 ajusta a tu DTO real
    const res = await createBooking(newBooking, token);
    setBookings([...bookings, res.data]);
    setTitle("");
  };

  const handleDelete = async (id) => {
    await deleteBooking(id, token);
    setBookings(bookings.filter((b) => b.id !== id));
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Reservas</h2>
      <div className="flex mb-4">
        <input
          type="text"
          placeholder="Nueva reserva"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="border p-2 rounded mr-2"
        />
        <button
          onClick={handleCreate}
          className="bg-blue-600 text-white px-4 py-2 rounded"
        >
          Crear
        </button>
      </div>
      <ul>
        {bookings.map((b) => (
          <li key={b.id} className="flex justify-between items-center mb-2">
            {b.title}
            <button
              onClick={() => handleDelete(b.id)}
              className="bg-red-500 text-white px-2 py-1 rounded"
            >
              Eliminar
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

