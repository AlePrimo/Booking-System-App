import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { FaUsers, FaBell, FaMoneyBillWave, FaServicestack } from "react-icons/fa";
import { useEffect, useState } from "react";
import { getNotifications } from "../../api/notificationService";

export default function DashboardAdmin() {
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
      const userNotifications = (res.data.content || res.data).filter(
        (n) => n.recipientId === user.id
      );
      const readIds = JSON.parse(localStorage.getItem("readNotifications") || "[]");
      const initialized = userNotifications.map((n) => ({
        ...n,
        readVisual: n.read || readIds.includes(n.id),
      }));
      setNotifications(initialized);
      setHasUnread(initialized.some((n) => !n.readVisual));
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    const handleStorage = () => {
      const readIds = JSON.parse(localStorage.getItem("readNotifications") || "[]");
      const unread = notifications.some((n) => !readIds.includes(n.id));
      setHasUnread(unread);
    };
    window.addEventListener("storage", handleStorage);
    return () => window.removeEventListener("storage", handleStorage);
  }, [notifications]);

  const cards = [
    {
      title: "Manejo de Usuarios",
      description: "Crear, editar, eliminar y buscar usuarios.",
      icon: <FaUsers size={30} className="text-indigo-600" />,
      path: "/admin/users",
    },
    {
      title: "Pagos",
      description: "Ver pagos recibidos y realizados, con filtros.",
      icon: <FaMoneyBillWave size={30} className="text-green-600" />,
      path: "/admin/payments",
    },
    {
      title: "Servicios",
      description: "Ver y filtrar todos los servicios publicados.",
      icon: <FaServicestack size={30} className="text-blue-600" />,
      path: "/admin/offerings",
    },
    {
      title: "Notificaciones",
      description: "Revisa tus notificaciones como administrador.",
      icon: <FaBell size={30} className={hasUnread ? "text-white" : "text-yellow-500"} />,
      path: "/notificaciones",
      highlight: hasUnread,
    },
  ];

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      <header className="flex flex-col sm:flex-row justify-between items-start sm:items-center px-6 py-4 bg-gray-100 shadow-sm gap-4 sm:gap-0">
        <h1 className="text-5xl font-bold text-indigo-600">BookingApp</h1>
        {user && (
          <div className="flex flex-col sm:flex-row items-start sm:items-center gap-2 sm:gap-4">
            <span className="font-medium">
              Bienvenido Admin, {user.name} ({user.email})
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

      <main className="flex-1 container mx-auto px-4 py-8">
        <h2 className="text-4xl font-bold text-center mb-12">Panel de Administración</h2>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {cards.map((card) => (
            <div
              key={card.title}
              onClick={() => navigate(card.path)}
              role="button"
              tabIndex={0}
              onKeyDown={(e) => {
                if (e.key === "Enter") navigate(card.path);
              }}
              className={`shadow-md rounded-lg p-6 cursor-pointer hover:shadow-xl transition transform hover:-translate-y-1
                ${card.title === "Notificaciones" && card.highlight ? "bg-red-500" : "bg-white"}`}
            >
              <div className="flex items-center gap-4 mb-4">
                {card.icon}
                <h3
                  className={`text-xl font-semibold ${
                    card.title === "Notificaciones" && card.highlight
                      ? "text-white"
                      : "text-black"
                  }`}
                >
                  {card.title}
                </h3>
              </div>
              {!card.highlight && (
                <p className="text-gray-600 text-sm">{card.description}</p>
              )}
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}
