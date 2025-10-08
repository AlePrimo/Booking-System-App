import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getNotifications, markAsRead, deleteNotification } from "../api/notificationService";

export default function CustomerNotifications() {
  const [notifications, setNotifications] = useState([]);
  const [selected, setSelected] = useState(null);
  const { user, token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user || !token) return;
    fetchNotifications();
  }, [user, token]);

  const fetchNotifications = async () => {
    try {
      const res = await getNotifications(0, 50, token);
      const filtered = (res.data.content || res.data).filter(n => n.recipientId === user.id);
      setNotifications(filtered);
    } catch (err) {
      console.error(err);
    }
  };

  const openNotification = async (notif) => {
    setSelected(notif);
    if (!notif.read) {
      await markAsRead(notif.id, token);
      fetchNotifications();
    }
  };

  const handleDelete = async (id) => {
    await deleteNotification(id, token);
    fetchNotifications();
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
          {notifications.map(n => (
            <li
              key={n.id}
              onClick={() => openNotification(n)}
              className={`p-4 rounded-lg shadow-md flex justify-between items-center cursor-pointer
                ${n.read ? "bg-green-100" : "bg-red-100 hover:bg-red-200"}`}
            >
              <div>
                <p className="font-semibold">{n.message}</p>
                <p className="text-sm text-gray-500">Tipo: {n.type}</p>
              </div>
              <button onClick={(e) => { e.stopPropagation(); handleDelete(n.id); }}
                className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600 transition">
                Eliminar
              </button>
            </li>
          ))}
        </ul>
      )}

      {/* Modal */}
      {selected && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white rounded-lg p-6 w-96 shadow-lg">
            <h3 className="text-xl font-bold mb-4">Notificación</h3>
            <p className="mb-4">{selected.message}</p>
            <button
              onClick={() => setSelected(null)}
              className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition"
            >
              Cerrar
            </button>
          </div>
        </div>
      )}
    </div>
  );
}


