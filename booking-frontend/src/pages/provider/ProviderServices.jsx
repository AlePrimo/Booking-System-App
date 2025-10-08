import { useEffect, useState } from "react";
import {
  getOfferings,
  createOffering,
  updateOffering,
  deleteOffering,
} from "../../api/offeringService";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { FaPlus, FaEdit, FaTrash } from "react-icons/fa";

export default function ProviderServices() {
  const { user, token } = useAuth();
  const navigate = useNavigate();

  const [offerings, setOfferings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [editingOffering, setEditingOffering] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    durationMinutes: "",
    price: "",
  });
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);

  // Cargar servicios del proveedor logueado
  const fetchOfferings = async () => {
    if (!user || !token) return;
    setLoading(true);
    setError("");
    try {
      const res = await getOfferings(page, size, token);
      const data = res.data;
      const all = data.content || data;
      const mine = all.filter((o) => o.providerId === user.id);
      setOfferings(mine);
      setTotalPages(data.totalPages || 1);
    } catch (err) {
      console.error(err);
      setError("Error al cargar los servicios");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOfferings();
  }, [page]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      ...formData,
      durationMinutes: parseInt(formData.durationMinutes),
      price: parseFloat(formData.price),
      providerId: user.id,
    };
    try {
      if (editingOffering) {
        await updateOffering(editingOffering.id, payload, token);
      } else {
        await createOffering(payload, token);
      }
      setShowForm(false);
      setEditingOffering(null);
      setFormData({ name: "", description: "", durationMinutes: "", price: "" });
      fetchOfferings();
    } catch (err) {
      console.error(err);
      alert("Error al guardar el servicio");
    }
  };

  const handleEdit = (offering) => {
    setEditingOffering(offering);
    setFormData({
      name: offering.name,
      description: offering.description,
      durationMinutes: offering.durationMinutes,
      price: offering.price,
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Seguro que deseas eliminar este servicio?")) return;
    try {
      await deleteOffering(id, token);
      fetchOfferings();
    } catch (err) {
      console.error(err);
      alert("Error al eliminar el servicio");
    }
  };

  if (loading) return <p className="p-6">Cargando servicios...</p>;
  if (error) return <p className="p-6 text-red-500">{error}</p>;

  return (
    <div className="p-6 min-h-screen bg-gray-100">
      {/* Botón Volver al Dashboard */}
      <button
        onClick={() => navigate("/dashboard-provider")}
        className="px-3 py-1 border rounded mb-6 hover:bg-gray-200 transition"
      >
        ← Volver al Dashboard
      </button>

      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold text-indigo-700">Mis Servicios</h2>
        <button
          onClick={() => {
            setShowForm(!showForm);
            setEditingOffering(null);
            setFormData({ name: "", description: "", durationMinutes: "", price: "" });
          }}
          className="flex items-center gap-2 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
        >
          <FaPlus /> {showForm ? "Cerrar" : "Nuevo Servicio"}
        </button>
      </div>

      {showForm && (
        <form
          onSubmit={handleSubmit}
          className="bg-white shadow-md rounded-lg p-6 mb-8 space-y-4"
        >
          <div>
            <label className="block font-semibold">Nombre</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              className="w-full border rounded px-3 py-2"
            />
          </div>
          <div>
            <label className="block font-semibold">Descripción</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              className="w-full border rounded px-3 py-2"
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block font-semibold">Duración (minutos)</label>
              <input
                type="number"
                name="durationMinutes"
                value={formData.durationMinutes}
                onChange={handleChange}
                required
                min="1"
                className="w-full border rounded px-3 py-2"
              />
            </div>
            <div>
              <label className="block font-semibold">Precio</label>
              <input
                type="number"
                step="0.01"
                name="price"
                value={formData.price}
                onChange={handleChange}
                required
                className="w-full border rounded px-3 py-2"
              />
            </div>
          </div>
          <button
            type="submit"
            className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 transition"
          >
            {editingOffering ? "Actualizar" : "Crear"}
          </button>
        </form>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {offerings.length === 0 ? (
          <p>No tienes servicios creados.</p>
        ) : (
          offerings.map((o) => (
            <div
              key={o.id}
              className="bg-white shadow rounded-lg p-4 flex justify-between items-center hover:shadow-lg transition"
            >
              <div>
                <h3 className="font-semibold text-lg text-indigo-600">{o.name}</h3>
                <p className="text-gray-600">{o.description}</p>
                <p className="text-gray-500 text-sm">
                  Duración: {o.durationMinutes} min | Precio: ${o.price}
                </p>
              </div>
              <div className="flex gap-3">
                <button
                  onClick={() => handleEdit(o)}
                  className="text-blue-600 hover:text-blue-800"
                >
                  <FaEdit />
                </button>
                <button
                  onClick={() => handleDelete(o.id)}
                  className="text-red-600 hover:text-red-800"
                >
                  <FaTrash />
                </button>
              </div>
            </div>
          ))
        )}
      </div>

      {offerings.length > 0 && (
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
    </div>
  );
}
