import { useEffect, useState } from "react";
import api from "../api";

export default function Notifications() {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    api.get("/notifications").then((res) => setNotifications(res.data.content || res.data));
  }, []);

  return (
    <div className="p-6">
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
