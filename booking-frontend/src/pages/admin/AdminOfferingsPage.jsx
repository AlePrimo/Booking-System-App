import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { getOfferings } from "../../api/offeringService";
import { FaArrowLeft, FaSearch } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

export default function AdminOfferingsPage() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [offerings, setOfferings] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [filter, setFilter] = useState("");

  useEffect(() => {
    if (token) fetchOfferings();
  }, [page, token]);

  const fetchOfferings = async () => {
    setLoading(true);
    try {
      const res = await getOfferings(page, 10, token);
      setOfferings(res.data.content || res.data);
      setTotalPages(res.data.totalPages || 1);
    } catch (err) {
      console.error("Error al cargar servicios:", err);
    } finally {
      setLoading(false);
    }
  };

  const filteredOfferings = offerings.filter((o) => {
    if (!filter) return true;
    return o.providerName?.toLowerCase().includes(filter.toLowerCase());
  });

  return (
    <div className="p-6 min-h-screen bg-gray-100">
      {/* 🔙 Volver */}
      <button
        onClick={() => navigate("/admin/dashboard")}
        className="px-3 py-1 border rounded mb-6 hover:bg-gray-200 transition"
      >
        <FaArrowLeft className="inline mr-2" />
        Volver al Dashboard
      </button>

      <div className="flex items-center gap-3 mb-6">
        <h1 className="text-3xl font-bold text-indigo-600">Servicios</h1>
      </div>

      {/* 🔍 Buscador */}
      <div className="flex mb-6 items-center border rounded px-2 max-w-md">
        <FaSearch className="text-gray-400 mr-2" />
        <input
          type="text"
          placeholder="Buscar por nombre de proveedor"
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
          className="flex-1 outline-none py-1"
        />
      </div>

      {loading ? (
        <p>Cargando servicios...</p>
      ) : (
        <div className="overflow-x-auto bg-white rounded shadow">
          <table className="w-full">
            <thead className="bg-indigo-600 text-white">
              <tr>
                <th className="p-3 text-left">ID</th>
                <th className="p-3 text-left">Proveedor</th>
                <th className="p-3 text-left">Nombre</th>
                <th className="p-3 text-left">Precio</th>
                <th className="p-3 text-left">Duración</th>
              </tr>
            </thead>
            <tbody>
              {filteredOfferings.length === 0 ? (
                <tr>
                  <td colSpan="5" className="p-4 text-center text-gray-500">
                    No hay servicios que coincidan con la búsqueda.
                  </td>
                </tr>
              ) : (
                filteredOfferings.map((o) => (
                  <tr key={o.id} className="border-b hover:bg-gray-50">
                    <td className="p-3">{o.id}</td>
                    <td className="p-3">{o.providerName || "N/A"}</td>
                    <td className="p-3">{o.name}</td>
                    <td className="p-3">${o.price}</td>
                    <td className="p-3">
                      {o.duration ? `${o.duration} min` : "N/A"}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      <div className="flex justify-center items-center mt-6 gap-4">
        <button
          onClick={() => setPage((p) => Math.max(0, p - 1))}
          disabled={page === 0}
          className={`px-4 py-2 rounded ${
            page === 0
              ? "bg-gray-300 cursor-not-allowed"
              : "bg-indigo-600 text-white hover:bg-indigo-700"
          }`}
        >
          Anterior
        </button>
        <span>
          Página {page + 1} de {totalPages}
        </span>
        <button
          onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
          disabled={page >= totalPages - 1}
          className={`px-4 py-2 rounded ${
            page >= totalPages - 1
              ? "bg-gray-300 cursor-not-allowed"
              : "bg-indigo-600 text-white hover:bg-indigo-700"
          }`}
        >
          Siguiente
        </button>
      </div>
    </div>
  );
}

