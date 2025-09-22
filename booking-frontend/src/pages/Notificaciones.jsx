import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import {
  getNotifications,
  createNotification,
  deleteNotification,
} from "../api/notificationService";

export default function Notificaciones() {
  const { token } = useAuth();
  const [notifications, setNotifications] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (token) {
      getNotifications(0, 10, token).then((res) => setNotifications(res.data.content));
    }
  }, [token]);

  const handleCreate = async () => {
    const newNotification = { message }; // 👈 ajustá DTO
    const res = await createNotification(newNotification, token);
    setNotifications([...notifications, res.data]);
    setMessage("");
  };

  const handleDelete = async (id) => {
    await deleteNotification(id, token);
    setNotifications(notifications.filter((n) => n.id !== id));
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Notificaciones</h2>
      <div className="flex mb-4">
        <input
          type="text"
          placeholder="Nueva notificación"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
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
        {notifications.map((n) => (
          <li key={n.id} className="flex justify-between items-center mb-2">
            {n.message}
            <button
              onClick={() => handleDelete(n.id)}
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
