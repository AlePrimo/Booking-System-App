import { useEffect, useState } from "react";
import { getOfferings } from "../../api/offeringService";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function Services() {
  const navigate = useNavigate();
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
      const response = await getOfferings(page, size, token);
      const data = response.data;
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

  return (
    <div className="p-6">
      <button
        onClick={() => navigate("/dashboard-customer")}
        className="px-3 py-1 border rounded mb-4"
      >
        ← Volver al Dashboard
      </button>

      <h2 className="text-2xl font-bold mb-4">Servicios disponibles</h2>

      {loading && <p>Cargando servicios...</p>}
      {error && <p className="text-red-500">{error}</p>}

      <ul className="space-y-2">
        {offerings.map((o) => (
          <li
            key={o.id}
            className="border p-2 rounded cursor-pointer hover:bg-gray-100"
            onClick={() => navigate(`/servicios/${o.id}`)}
          >
            {o.name} - ${o.price}
          </li>
        ))}
      </ul>

      {/* PAGINACIÓN */}
      {totalPages > 1 && (
        <div className="flex gap-2 mt-4 items-center">
          <button
            onClick={() => page > 0 && setPage(page - 1)}
            disabled={page === 0}
            className="px-3 py-1 border rounded disabled:opacity-50"
          >
            Anterior
          </button>
          <button
            onClick={() => page + 1 < totalPages && setPage(page + 1)}
            disabled={page + 1 >= totalPages}
            className="px-3 py-1 border rounded disabled:opacity-50"
          >
            Siguiente
          </button>
          <span className="ml-2">
            Página {page + 1} de {totalPages}
          </span>
        </div>
      )}
    </div>
  );
}
