import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import {
  getOffers,
  createOffer,
  deleteOffer,
} from "../api/offerService";

export default function Ofertas() {
  const { token } = useAuth();
  const [offers, setOffers] = useState([]);
  const [title, setTitle] = useState("");

  useEffect(() => {
    if (token) {
      getOffers(0, 10, token).then((res) => setOffers(res.data.content));
    }
  }, [token]);

  const handleCreate = async () => {
    const newOffer = { title, discount: 10 }; // 👈 ajustá DTO
    const res = await createOffer(newOffer, token);
    setOffers([...offers, res.data]);
    setTitle("");
  };

  const handleDelete = async (id) => {
    await deleteOffer(id, token);
    setOffers(offers.filter((o) => o.id !== id));
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Ofertas</h2>
      <div className="flex mb-4">
        <input
          type="text"
          placeholder="Nueva oferta"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="border p-2 rounded mr-2"
        />
        <button
          onClick={handleCreate}
          className="bg-blue-600 text-white px-4 py-2 rounded"
        >
          Crear
        </button>
      </div>
      <ul>
        {offers.map((o) => (
          <li key={o.id} className="flex justify-between items-center mb-2">
            {o.title} - {o.discount}%
            <button
              onClick={() => handleDelete(o.id)}
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
