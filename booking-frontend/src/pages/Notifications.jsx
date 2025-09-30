import { useEffect, useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

export default function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    api.get("/notifications").then((res) => setNotifications(res.data.content || res.data));
  }, []);

  return (
    <div className="p-6">
      <button
        onClick={() => navigate("/dashboard-customer")}
        className="px-3 py-1 border rounded mb-4"
      >
        ← Volver al Dashboard
      </button>

      <h2 className="text-2xl font-bold mb-4">Notificaciones</h2>
      <ul className="space-y-2">
        {notifications.map((n) => (
          <li key={n.id} className="border p-2 rounded">
            {n.message} ({n.type}) - Enviado: {n.sent ? "Sí" : "No"}
          </li>
        ))}
      </ul>
    </div>
  );
}
