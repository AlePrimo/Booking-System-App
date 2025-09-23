import { useEffect, useState } from "react";
import api from "../services/api";

export default function Payments() {
  const [payments, setPayments] = useState([]);

  useEffect(() => {
    api.get("/payments").then((res) => setPayments(res.data.content || res.data));
  }, []);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Pagos</h2>
      <ul className="space-y-2">
        {payments.map((p) => (
          <li key={p.id} className="border p-2 rounded">
            Pago #{p.id} - Reserva {p.bookingId} - ${p.amount}
          </li>
        ))}
      </ul>
    </div>
  );
}

