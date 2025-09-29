import { useEffect, useState } from "react";
import { getOfferings } from "../services/offeringService";
import { useAuth } from "../context/AuthContext";

export default function Offerings() {
  const { user } = useAuth();
  const [offerings, setOfferings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);

  const fetchOfferings = async () => {
    setLoading(true);
    setError("");
    try {
      const token = localStorage.getItem("accessToken");
      const res = await getOfferings(page, size, token);
      const data = res.data;
      setOfferings(data.content || data); // soporta paginación
      setTotalPages(data.totalPages || 1);
    } catch (err) {
      setError(err.response?.data?.message || "Error al cargar los servicios");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOfferings();
  }, [page]);

  const handlePrev = () => setPage((p) => Math.max(p - 1, 0));
  const handleNext = () => setPage((p) => Math.min(p + 1, totalPages - 1));

  if (loading) return <p className="p-6">Cargando servicios...</p>;
  if (error) return <p className="p-6 text-red-500">{error}</p>;

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Servicios disponibles</h2>
      <ul className="space-y-2">
        {offerings.map((o) => (
          <li key={o.id} className="border p-2 rounded">
            <p className="font-semibold">{o.name}</p>
            <p>Precio: ${o.price}</p>
            <p>Proveedor: {o.providerName || o.provider?.name}</p>
          </li>
        ))}
      </ul>

      <div className="flex justify-between mt-4">
        <button
          onClick={handlePrev}
          disabled={page === 0}
          className="px-4 py-2 bg-indigo-600 text-white rounded disabled:opacity-50"
        >
          Anterior
        </button>
        <span>
          Página {page + 1} de {totalPages}
        </span>
        <button
          onClick={handleNext}
          disabled={page + 1 >= totalPages}
          className="px-4 py-2 bg-indigo-600 text-white rounded disabled:opacity-50"
        >
          Siguiente
        </button>
      </div>
    </div>
  );
}

