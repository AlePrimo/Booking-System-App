import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import {
  getPayments,
  createPayment,
  deletePayment,
} from "../api/paymentService";

export default function Pagos() {
  const { token } = useAuth();
  const [payments, setPayments] = useState([]);
  const [amount, setAmount] = useState("");

  useEffect(() => {
    if (token) {
      getPayments(0, 10, token).then((res) => setPayments(res.data.content));
    }
  }, [token]);

  const handleCreate = async () => {
    const newPayment = { amount, method: "CARD" }; // 👈 ajustá DTO
    const res = await createPayment(newPayment, token);
    setPayments([...payments, res.data]);
    setAmount("");
  };

  const handleDelete = async (id) => {
    await deletePayment(id, token);
    setPayments(payments.filter((p) => p.id !== id));
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Pagos</h2>
      <div className="flex mb-4">
        <input
          type="number"
          placeholder="Monto"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          className="border p-2 rounded mr-2"
        />
        <button
          onClick={handleCreate}
          className="bg-blue-600 text-white px-4 py-2 rounded"
        >
          Registrar
        </button>
      </div>
      <ul>
        {payments.map((p) => (
          <li key={p.id} className="flex justify-between items-center mb-2">
            ${p.amount}
            <button
              onClick={() => handleDelete(p.id)}
              className="bg-red-500 text-white px-2 py-1 rounded"
            >
              Eliminar
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
