import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getNotifications, markAsRead, deleteNotification } from "../api/notificationService";

export default function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const { user, token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user || !token) return;
    fetchNotifications();
  }, [user, token]);

  const fetchNotifications = async () => {
    try {
      const res = await getNotifications(0, 50, token); // Trae todas
      // Filtrar solo las del usuario logueado
      const filtered = (res.data.content || res.data).filter(
        (n) => n.recipientId === user.id
      );
      setNotifications(filtered);
    } catch (err) {
      console.error("Error fetching notifications:", err);
    }
  };

  const handleMarkAsRead = async (id) => {
    try {
      await markAsRead(id, token);
      fetchNotifications();
    } catch (err) {
      console.error(err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteNotification(id, token);
      fetchNotifications();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6">
      <button onClick={() => navigate("/dashboard-customer")} className="px-4 py-2 mb-6 rounded bg-gray-200 hover:bg-gray-300 transition">
        ← Volver al Dashboard
      </button>

      <h2 className="text-3xl font-bold mb-6 text-indigo-600">Notificaciones</h2>

      {notifications.length === 0 ? (
        <p className="text-gray-500">No tienes notificaciones aún.</p>
      ) : (
        <ul className="space-y-4">
          {notifications.map((n) => (
            <li key={n.id} className={`p-4 rounded-lg shadow-md flex justify-between items-center ${n.read ? "bg-gray-100" : "bg-yellow-50"}`}>
              <div>
                <p className="font-semibold">{n.message}</p>
                <p className="text-sm text-gray-500">Tipo: {n.type}</p>
              </div>
              <div className="flex gap-2">
                {!n.read && (
                  <button onClick={() => handleMarkAsRead(n.id)} className="px-3 py-1 bg-green-500 text-white rounded hover:bg-green-600 transition">
                    Marcar como leído
                  </button>
                )}
                <button onClick={() => handleDelete(n.id)} className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600 transition">
                  Eliminar
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}


