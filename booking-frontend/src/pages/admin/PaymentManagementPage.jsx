import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { getPayments, deletePayment } from "../../api/paymentService";
import { FaArrowLeft, FaSearch, FaList, FaTrash } from "react-icons/fa";

export default function PaymentManagementPage() {
  const { token } = useAuth();
  const navigate = useNavigate();

  const [mode, setMode] = useState("list"); // list | searchProvider | searchCustomer
  const [payments, setPayments] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [searchValue, setSearchValue] = useState("");

  const [toast, setToast] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchPayments = async () => {
    if (!token) return;
    setLoading(true);
    setError("");
    try {
      const res = await getPayments(page, size, token);
      const data = res.data;
      setPayments(data.content || data);
      setFiltered(data.content || data);
      setTotalPages(data.totalPages || 1);

    } catch (err) {
      console.error(err);
      setError("Error al obtener pagos");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (mode === "list") fetchPayments();
  }, [page, mode]);

  const showToast = (msg) => {
    setToast(msg);
    setTimeout(() => setToast(""), 3000);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Eliminar este pago?")) return;
    try {
      await deletePayment(id, token);
      showToast("Pago eliminado correctamente");
      fetchPayments();
    } catch (err) {
      console.error(err);
      alert("Error al eliminar el pago");
    }
  };

  const handleSearch = () => {
    if (!searchValue.trim()) {
      setFiltered(payments);
      return;
    }
    const lower = searchValue.toLowerCase();
    let result = [];
    if (mode === "searchProvider") {
      result = payments.filter(p =>
        p.providerName?.toLowerCase().includes(lower)
      );
    } else if (mode === "searchCustomer") {
      result = payments.filter(p =>
        p.customerEmail?.toLowerCase().includes(lower)
      );
    }
    setFiltered(result);
  };

  return (
    <div className="p-6 min-h-screen bg-gray-100 relative">
      {/* ✅ Toast */}
      {toast && (
        <div className="fixed top-5 right-5 bg-green-600 text-white px-4 py-2 rounded shadow-lg transition">
          {toast}
        </div>
      )}

      {/* 🔙 Volver */}
      <button
        onClick={() => navigate("/admin/dashboard")}
        className="px-3 py-1 border rounded mb-6 hover:bg-gray-200 transition"
      >
        <FaArrowLeft className="inline mr-2" />
        Volver al Dashboard
      </button>

      {/* 🧭 Selector de modo */}
      <div className="flex flex-wrap gap-4 mb-6">
        <button
          onClick={() => {
            setMode("list");
            setSearchValue("");
            fetchPayments();
          }}
          className={`flex items-center gap-2 px-4 py-2 rounded ${
            mode === "list"
              ? "bg-indigo-600 text-white"
              : "bg-white text-gray-800 border hover:bg-gray-100"
          }`}
        >
          <FaList /> Listar Pagos
        </button>
        <button
          onClick={() => {
            setMode("searchProvider");
            setSearchValue("");
          }}
          className={`flex items-center gap-2 px-4 py-2 rounded ${
            mode === "searchProvider"
              ? "bg-indigo-600 text-white"
              : "bg-white text-gray-800 border hover:bg-gray-100"
          }`}
        >
          <FaSearch /> Buscar por Provider
        </button>
        <button
          onClick={() => {
            setMode("searchCustomer");
            setSearchValue("");
          }}
          className={`flex items-center gap-2 px-4 py-2 rounded ${
            mode === "searchCustomer"
              ? "bg-indigo-600 text-white"
              : "bg-white text-gray-800 border hover:bg-gray-100"
          }`}
        >
          <FaSearch /> Buscar por Customer
        </button>
      </div>

      {/* 🔍 Buscador */}
      {(mode === "searchProvider" || mode === "searchCustomer") && (
        <div className="mb-6 flex gap-2">
          <input
            type="text"
            placeholder={
              mode === "searchProvider" ? "Nombre del provider..." : "Email del customer..."
            }
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
            className="flex-1 border rounded px-3 py-2"
          />
          <button
            onClick={handleSearch}
            className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 transition"
          >
            Buscar
          </button>
        </div>
      )}

      {/* 🧾 Listado */}
      {loading ? (
        <p>Cargando...</p>
      ) : error ? (
        <p className="text-red-500">{error}</p>
      ) : filtered.length === 0 ? (
        <p>No hay pagos registrados.</p>
      ) : (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filtered.map((payment) => (
              <div
                key={payment.id}
                className="bg-white shadow rounded-lg p-4 flex justify-between items-center hover:shadow-lg transition"
              >
                <div>
                  <h3 className="font-semibold text-lg text-indigo-600">
                    Pago #{payment.id}
                  </h3>
                  <p className="text-gray-600">
                    Provider: {payment.providerName || "N/A"}
                  </p>
                  <p className="text-gray-500 text-sm">
                    Customer: {payment.customerEmail || "N/A"}
                  </p>
                  <p className="text-gray-500 text-sm">
                    Monto: ${payment.amount}
                  </p>
                  <p className="text-gray-500 text-sm">
                    Estado: {payment.status}
                  </p>
                </div>
                <div className="flex gap-3">
                  <button
                    onClick={() => handleDelete(payment.id)}
                    className="text-red-600 hover:text-red-800"
                  >
                    <FaTrash />
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* 📄 Paginación */}
          {mode === "list" && filtered.length > 0 && (
            <div className="flex justify-between items-center mt-6">
              <button
                onClick={() => setPage((p) => Math.max(p - 1, 0))}
                disabled={page === 0}
                className="px-4 py-2 bg-gray-300 rounded disabled:opacity-50"
              >
                Anterior
              </button>
              <span>
                Página {page + 1} de {totalPages}
              </span>
              <button
                onClick={() => setPage((p) => Math.min(p + 1, totalPages - 1))}
                disabled={page + 1 >= totalPages}
                className="px-4 py-2 bg-gray-300 rounded disabled:opacity-50"
              >
                Siguiente
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}
