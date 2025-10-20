import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useEffect, useState } from "react";
import { getNotifications } from "../../api/notificationService";
import { FaArrowLeft } from "react-icons/fa";

export default function NotificationsPage() {
  const { user, token } = useAuth();
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    if (!user || !token) return;
    fetchNotifications();
  }, [user, token]);

  const fetchNotifications = async () => {
    try {
      const res = await getNotifications(0, 50, token);
      const userNotifications = (res.data.content || res.data).filter(
        (n) => n.recipientId === user.id
      );
      setNotifications(userNotifications);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6 min-h-screen bg-gray-100 relative">
      {/* 🔙 Botón volver (estilo unificado con otras páginas) */}
      <button
        onClick={() => navigate("/admin/dashboard")}
        className="px-3 py-1 border rounded mb-6 hover:bg-gray-200 transition"
      >
        <FaArrowLeft className="inline mr-2" />
        Volver al Dashboard
      </button>

      {/* 🧭 Título principal */}
      <h1 className="text-3xl font-bold text-indigo-600 mb-6">Notificaciones</h1>

      {/* 📨 Listado de notificaciones */}
      {notifications.length === 0 ? (
        <p className="text-gray-600 text-lg text-center">
          No tienes notificaciones por el momento.
        </p>
      ) : (
        <ul className="space-y-4">
          {notifications.map((n) => (
            <li
              key={n.id}
              className="bg-white shadow rounded-lg p-4 border border-gray-200 hover:shadow-lg transition"
            >
              <p className="font-semibold text-indigo-600">{n.title || "Notificación"}</p>
              <p className="text-gray-700">{n.message}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
