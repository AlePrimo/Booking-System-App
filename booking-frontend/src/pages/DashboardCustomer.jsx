import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getOfferings } from "../api/offeringService"; // ✅ usar la versión de api que maneja token y /api

export default function DashboardCustomer() {
  const { user } = useAuth();
  const [offerings, setOfferings] = useState([]);
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [totalPages, setTotalPages] = useState(0);

  const fetchOfferings = async () => {
    setLoading(true);
    setError(null);
    try {
      const token = localStorage.getItem("accessToken");
      const response = await getOfferings(page, size, token); // ✅ llama al service correcto
      const data = response.data;

      // Manejar Spring Page
      setOfferings(data.content || data);
      setTotalPages(data.totalPages || 1);
    } catch (err) {
      console.error("Error cargando offerings:", err);
      setError("No se pudieron cargar los servicios.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOfferings();
  }, [page]);

  const nextPage = () => {
    if (page + 1 < totalPages) setPage(page + 1);
  };

  const prevPage = () => {
    if (page > 0) setPage(page - 1);
  };

  return (
    <div className="p-6">
      <h1 className="text-xl font-bold">Panel de Cliente</h1>
      {user && <p>Bienvenido, {user.name} ({user.email})</p>}
      <p>Aquí podrás ver tus reservas, notificaciones y perfil.</p>

      <h2 className="text-2xl font-bold mt-6 mb-4">Servicios disponibles</h2>

      {loading && <p>Cargando servicios...</p>}
      {error && <p className="text-red-500">{error}</p>}

      <ul className="space-y-2">
        {offerings.map((o) => (
          <li key={o.id} className="border p-2 rounded">
            {o.name} - ${o.price}
          </li>
        ))}
      </ul>

      {totalPages > 1 && (
        <div className="flex gap-2 mt-4 items-center">
          <button
            onClick={prevPage}
            disabled={page === 0}
            className="px-3 py-1 border rounded disabled:opacity-50"
          >
            Anterior
          </button>
          <button
            onClick={nextPage}
            disabled={page + 1 >= totalPages}
            className="px-3 py-1 border rounded disabled:opacity-50"
          >
            Siguiente
          </button>
          <span className="ml-2">Página {page + 1} de {totalPages}</span>
        </div>
      )}
    </div>
  );
}
