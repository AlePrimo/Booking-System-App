import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getNotifications } from "../api/notificationService";
import { useNavigate } from "react-router-dom";

export default function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const { token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) return;
    fetchNotifications();
  }, [token]);

  const fetchNotifications = async () => {
    try {
      const res = await getNotifications(0, 50, token);
      setNotifications(res.data.content || res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const getColor = (type) => {
    switch (type) {
      case "RESERVATION":
        return "bg-indigo-100 text-indigo-700";
      case "PAYMENT":
        return "bg-green-100 text-green-700";
      case "PAYMENT_REJECTED":
        return "bg-red-100 text-red-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  return (
    <div className="p-6">
      <button
        onClick={() => navigate("/dashboard-customer")}
        className="px-3 py-1 border rounded mb-6 hover:bg-gray-200 transition"
      >
        ← Volver al Dashboard
      </button>

      <h2 className="text-3xl font-bold mb-6 text-indigo-700">Notificaciones</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {notifications.map((n) => (
          <div
            key={n.id}
            className={`border rounded-lg p-4 shadow ${getColor(
              n.type
            )} hover:shadow-lg transition`}
          >
            <p className="font-medium">{n.message}</p>
            <p className="text-sm mt-1">
              <strong>Tipo:</strong> {n.type}
            </p>
            <p className="text-sm mt-1">
              <strong>Enviado:</strong> {n.sent ? "Sí" : "No"}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}

