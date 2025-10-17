import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getUserById, updateUser } from "../../api/userService";
import { useAuth } from "../../context/AuthContext";

export default function EditUserPage() {
  const { id } = useParams();
  const { token } = useAuth();
  const navigate = useNavigate();

  const [userData, setUserData] = useState({ name: "", email: "", roles: [] });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchUser = async () => {
      try {
        setLoading(true);
        const res = await getUserById(id, token);
        setUserData({
          name: res.data.name,
          email: res.data.email,
          roles: res.data.roles || [],
        });
      } catch (err) {
        console.error(err);
        setError("Error al cargar usuario");
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [id, token]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      await updateUser(id, userData, token);
      navigate("/admin/users"); // Volvemos a la lista
    } catch (err) {
      console.error(err);
      setError("Error al actualizar usuario");
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <p className="p-6">Cargando usuario...</p>;
  if (error) return <p className="p-6 text-red-500">{error}</p>;

  return (
    <div className="p-6 min-h-screen bg-gray-100">
      <button
        onClick={() => navigate("/admin/users")}
        className="mb-4 px-3 py-1 bg-gray-300 rounded hover:bg-gray-400 transition"
      >
        ← Volver a Gestión de Usuarios
      </button>

      <div className="max-w-md mx-auto bg-white shadow rounded-lg p-6">
        <h2 className="text-2xl font-bold text-indigo-600 mb-4">
          Editar Usuario #{id}
        </h2>

        <form onSubmit={handleSave} className="space-y-4">
          <div>
            <label className="block font-semibold">Nombre</label>
            <input
              type="text"
              name="name"
              value={userData.name}
              onChange={handleChange}
              className="w-full border rounded px-3 py-2"
              required
            />
          </div>

          <div>
            <label className="block font-semibold">Email</label>
            <input
              type="email"
              name="email"
              value={userData.email}
              onChange={handleChange}
              className="w-full border rounded px-3 py-2"
              required
            />
          </div>

          {/* Roles (solo texto editable por ahora, puedes cambiarlo a multi-select más adelante) */}
          <div>
            <label className="block font-semibold">Roles (separados por coma)</label>
            <input
              type="text"
              name="roles"
              value={userData.roles.join(", ")}
              onChange={(e) =>
                setUserData({ ...userData, roles: e.target.value.split(",").map(r => r.trim()) })
              }
              className="w-full border rounded px-3 py-2"
            />
          </div>

          <button
            type="submit"
            disabled={saving}
            className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 transition"
          >
            {saving ? "Guardando..." : "Guardar Cambios"}
          </button>
        </form>
      </div>
    </div>
  );
}
