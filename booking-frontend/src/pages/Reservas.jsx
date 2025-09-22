import { useEffect, useState } from "react";
import api from "../api";

export default function Bookings() {
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    api.get("/bookings").then((res) => setBookings(res.data.content || res.data));
  }, []);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Reservas</h2>
      <ul className="space-y-2">
        {bookings.map((b) => (
          <li key={b.id} className="border p-2 rounded">
            Reserva #{b.id} - Cliente {b.customerId} - {b.status}
          </li>
        ))}
      </ul>
    </div>
  );
}

