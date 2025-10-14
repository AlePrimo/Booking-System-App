import { useEffect, useState } from "react";
import { getPayments } from "../api/paymentService";
import { useNavigate } from "react-router-dom";

export default function ProviderPagos() {
  const [payments, setPayments] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  useEffect(() => {
    loadPayments();
  }, [page]);

  const loadPayments = async () => {
    try {
      const { data } = await getPayments(page, 10, token);
      setPayments(data.content);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error("Error al cargar pagos", error);
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-800">💳 Pagos Recibidos</h1>
        <button
          onClick={() => navigate("/dashboard")}
          className="bg-gray-800 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition"
        >
          Volver al Dashboard
        </button>
      </div>

      <div className="overflow-x-auto bg-white shadow rounded-lg">
        <table className="min-w-full text-sm text-gray-700">
          <thead className="bg-gray-100 text-left">
            <tr>
              <th className="px-4 py-2">ID</th>
              <th className="px-4 py-2">Reserva</th>
              <th className="px-4 py-2">Monto</th>
              <th className="px-4 py-2">Método</th>
              <th className="px-4 py-2">Estado</th>
            </tr>
          </thead>
          <tbody>
            {payments.length > 0 ? (
              payments.map((payment) => (
                <tr key={payment.id} className="border-b hover:bg-gray-50">
                  <td className="px-4 py-2">{payment.id}</td>
                  <td className="px-4 py-2">{payment.bookingId}</td>
                  <td className="px-4 py-2">${payment.amount.toFixed(2)}</td>
                  <td className="px-4 py-2">{payment.method}</td>
                  <td className="px-4 py-2">
                    <span
                      className={`px-2 py-1 rounded-full text-xs font-semibold ${
                        payment.status === "PAID"
                          ? "bg-green-100 text-green-700"
                          : payment.status === "PENDING"
                          ? "bg-yellow-100 text-yellow-700"
                          : "bg-red-100 text-red-700"
                      }`}
                    >
                      {payment.status}
                    </span>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5" className="text-center py-4 text-gray-500">
                  No hay pagos registrados
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* 📌 Paginación */}
      <div className="flex justify-center items-center mt-4 gap-2">
        <button
          onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
          disabled={page === 0}
          className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
        >
          Anterior
        </button>
        <span>
          Página {page + 1} de {totalPages}
        </span>
        <button
          onClick={() => setPage((prev) => Math.min(prev + 1, totalPages - 1))}
          disabled={page >= totalPages - 1}
          className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
        >
          Siguiente
        </button>
      </div>
    </div>
  );
}
