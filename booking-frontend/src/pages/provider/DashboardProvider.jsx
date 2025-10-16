import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { FaServicestack, FaCalendarAlt, FaBell, FaMoneyBillWave } from "react-icons/fa";
import { useEffect, useState } from "react";
import { getNotifications } from "../api/notificationService";

export default function DashboardProvider() {
  const { user, logout, token } = useAuth();
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState([]);
  const [hasUnread, setHasUnread] = useState(false);

  useEffect(() => {
    if (!user || !token) return;
    fetchNotifications();
  }, [user, token]);

  const fetchNotifications = async () => {
    try {
      const res = await getNotifications(0, 50, token);
      // Filtra solo notificaciones para el provider logueado
      const userNotifications = (res.data.content || res.data).filter(
        (n) => n.recipientId === user.id
      );

      const readIds = JSON.parse(localStorage.getItem("readProviderNotifications") || "[]");

      const initialized = userNotifications.map((n) => ({
        ...n,
        readVisual: readIds.includes(n.id)
      }));

      setNotifications(initialized);
      const unread = initialized.some((n) => !n.readVisual);
      setHasUnread(unread);
    } catch (err) {
      console.error(err);
    }
  };

  // Sincroniza cambios locales de lectura
  useEffect(() => {
    const handleStorage = () => {
      const readIds = JSON.parse(localStorage.getItem("readProviderNotifications") || "[]");
      const unread = notifications.some((n) => !readIds.includes(n.id));
      setHasUnread(unread);
    };
    window.addEventListener("storage", handleStorage);
    return () => window.removeEventListener("storage", handleStorage);
  }, [notifications]);

  const cards = [
    {
      title: "Servicios",
      description: "Gestiona tus servicios (crear, editar o eliminar).",
      icon: <FaServicestack size={30} className="text-green-500" />,
      path: "/provider/servicios",
    },
    {
      title: "Reservas",
      description: "Visualiza las reservas que recibiste de tus clientes.",
      icon: <FaCalendarAlt size={30} className="text-indigo-600" />,
      path: "/provider/reservas",
    },
    {
      title: "Notificaciones",
      description: "Revisa las notificaciones de tus clientes.",
      icon: <FaBell size={30} className={hasUnread ? "text-white" : "text-yellow-500"} />,
      path: "/provider/notificaciones",
      highlight: hasUnread
    },
    {
      title: "Pagos",
      description: "Consulta el historial de pagos recibidos.",
      icon: <FaMoneyBillWave size={30} className="text-purple-600" />,
      path: "/provider/pagos",
    },
  ];

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      {/* Header */}
      <header className="flex flex-col sm:flex-row justify-between items-start sm:items-center px-6 py-4 bg-gray-100 shadow-sm gap-4 sm:gap-0">
        <h1 className="text-5xl font-bold text-indigo-600">BookingApp</h1>
        {user && (
          <div className="flex flex-col sm:flex-row items-start sm:items-center gap-2 sm:gap-4">
            <span className="font-medium">
              Bienvenido, {user.name} ({user.email})
            </span>
            <button
              onClick={logout}
              className="px-3 py-1 rounded bg-red-500 text-white hover:bg-red-600 transition"
            >
              Logout
            </button>
          </div>
        )}
      </header>

      {/* Main */}
      <main className="flex-1 container mx-auto px-4 py-8">
        <h2 className="text-4xl font-bold text-center mb-12">Panel de Proveedor</h2>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
          {cards.map((card) => (
            <div
              key={card.title}
              onClick={() => navigate(card.path)}
              className={`shadow-md rounded-lg p-4 sm:p-6 cursor-pointer hover:shadow-xl transition transform hover:-translate-y-1
                ${card.title === "Notificaciones" && card.highlight ? "bg-red-500" : "bg-white"}`}
            >
              <div className="flex items-center gap-3 sm:gap-4 mb-2 sm:mb-4">
                {card.icon}
                <h3 className={`text-lg sm:text-xl font-semibold ${card.title === "Notificaciones" && card.highlight ? "text-white" : "text-black"}`}>
                  {card.title}
                </h3>
              </div>
              {!card.highlight && <p className="text-sm sm:text-gray-600">{card.description}</p>}
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}
