import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { FaCalendarAlt, FaBell, FaServicestack } from "react-icons/fa"; // Íconos

export default function DashboardCustomer() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const cards = [
    {
      title: "Reservas",
      description: "Visualiza tus reservas actuales y pasadas.",
      icon: <FaCalendarAlt size={30} className="text-indigo-600" />,
      path: "/reservas",
    },
    {
      title: "Notificaciones",
      description: "Revisa tus notificaciones y alertas.",
      icon: <FaBell size={30} className="text-yellow-500" />,
      path: "/notificaciones",
    },
    {
      title: "Servicios",
      description: "Consulta los servicios disponibles y sus detalles.",
      icon: <FaServicestack size={30} className="text-green-500" />,
      path: "/servicios",
    },
  ];

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      {/* HEADER */}
<header className="flex justify-between items-center px-6 py-4 bg-gray-100">
  <h1 className="text-5xl font-bold text-indigo-600">BookingApp</h1>
  {user && (
    <div className="flex items-center gap-4">
      <span className="font-Medium">
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


      {/* CONTENIDO PRINCIPAL */}
      <main className="flex-1 container mx-auto px-4 py-8">
        <h2 className="text-4xl font-bold text-center mb-12">Panel de Cliente</h2>

        {/* TARJETAS */}
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {cards.map((card) => (
            <div
              key={card.title}
              onClick={() => navigate(card.path)}
              className="bg-white shadow-md rounded-lg p-6 cursor-pointer hover:shadow-xl transition transform hover:-translate-y-1"
            >
              <div className="flex items-center gap-4 mb-4">
                {card.icon}
                <h3 className="text-xl font-semibold">{card.title}</h3>
              </div>
              <p className="text-gray-600">{card.description}</p>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}
