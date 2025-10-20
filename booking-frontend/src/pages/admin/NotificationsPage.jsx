import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useEffect, useState } from "react";
import { getNotifications } from "../../api/notificationService";

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
    <div className="flex flex-col min-h-screen bg-gray-100">
      <header className="flex justify-between items-center px-6 py-4 bg-white shadow">
        <h1 className="text-3xl font-bold text-indigo-600">Notificaciones</h1>
        <button
          onClick={() => navigate("/admin/dashboard")}
          className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition"
        >
          Volver al Dashboard
        </button>
      </header>

      <main className="flex-1 container mx-auto px-4 py-8">
        {notifications.length === 0 ? (
          <p className="text-gray-600 text-lg text-center">
            No tienes notificaciones por el momento.
          </p>
        ) : (
          <ul className="space-y-4">
            {notifications.map((n) => (
              <li
                key={n.id}
                className="bg-white shadow rounded-lg p-4 border border-gray-200"
              >
                <p className="font-semibold">{n.title || "Notificación"}</p>
                <p className="text-gray-700">{n.message}</p>
              </li>
            ))}
          </ul>
        )}
      </main>
    </div>
  );
}
