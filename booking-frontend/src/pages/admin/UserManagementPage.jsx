import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { getUsers, getUserById, getUserByEmail, deleteUser } from "../../api/userService";
import { FaArrowLeft, FaTrash, FaEdit, FaSearch, FaList } from "react-icons/fa";

export default function UserManagementPage() {
  const { token } = useAuth();
  const navigate = useNavigate();

  const [mode, setMode] = useState("list"); // list | searchId | searchEmail
  const [users, setUsers] = useState([]);
  const [singleUser, setSingleUser] = useState(null);

  const [searchValue, setSearchValue] = useState("");
  const [toast, setToast] = useState("");

  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");


  // 🪄 Formatear el nombre del rol para mostrarlo más legible
  const formatRole = (role) => {
    if (!role) return "Sin rol";
    switch (role) {
      case "ROLE_ADMIN":
        return "Administrador";
      case "ROLE_PROVIDER":
        return "Proveedor";
      case "ROLE_CUSTOMER":
        return "Cliente";
      default:
        return role;
    }
  };


  // 📥 Listar usuarios paginados
  const fetchUsers = async () => {
    if (!token) return;
    setLoading(true);
    setError("");
    try {
      const res = await getUsers(page, size, token);
      const data = res.data;
      setUsers(data.content || data);
      setTotalPages(data.totalPages || 1);
    } catch (err) {
      console.error(err);
      setError("Error al obtener usuarios");
    } finally {
      setLoading(false);
    }
  };

  // 📥 Buscar por ID
  const fetchUserById = async () => {
    if (!searchValue || !token) return;
    setLoading(true);
    setError("");
    try {
      const res = await getUserById(searchValue, token);
      setSingleUser(res.data);
    } catch (err) {
      console.error(err);
      setError("No se encontró el usuario");
    } finally {
      setLoading(false);
    }
  };

  // 📥 Buscar por Email
  const fetchUserByEmail = async () => {
    if (!searchValue || !token) return;
    setLoading(true);
    setError("");
    try {
      const res = await getUserByEmail(searchValue, token);
      setSingleUser(res.data);
    } catch (err) {
      console.error(err);
      setError("No se encontró el usuario");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (mode === "list") fetchUsers();
  }, [page, mode]);

  // 🧽 Toast
  const showToast = (message) => {
    setToast(message);
    setTimeout(() => setToast(""), 3000);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Estás seguro de eliminar este usuario?")) return;
    try {
      await deleteUser(id, token);
      showToast("Usuario eliminado correctamente");
      if (mode === "list") fetchUsers();
      else setSingleUser(null);
    } catch (err) {
      console.error(err);
      alert("Error al eliminar el usuario");
    }
  };

  const handleEdit = (id) => {
    navigate(`/admin/users/${id}/edit`);
  };

  return (
    <div className="p-6 min-h-screen bg-gray-100 relative">
      {/* ✅ Toast */}
      {toast && (
        <div className="fixed top-5 right-5 bg-green-600 text-white px-4 py-2 rounded shadow-lg transition">
          {toast}
        </div>
      )}

      {/* 🔙 Botón volver */}
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
            setSingleUser(null);
            setSearchValue("");
          }}
          className={`flex items-center gap-2 px-4 py-2 rounded ${
            mode === "list"
              ? "bg-indigo-600 text-white"
              : "bg-white text-gray-800 border hover:bg-gray-100"
          }`}
        >
          <FaList /> Listar usuarios
        </button>
        <button
          onClick={() => {
            setMode("searchId");
            setSingleUser(null);
            setSearchValue("");
          }}
          className={`flex items-center gap-2 px-4 py-2 rounded ${
            mode === "searchId"
              ? "bg-indigo-600 text-white"
              : "bg-white text-gray-800 border hover:bg-gray-100"
          }`}
        >
          <FaSearch /> Buscar por ID
        </button>
        <button
          onClick={() => {
            setMode("searchEmail");
            setSingleUser(null);
            setSearchValue("");
          }}
          className={`flex items-center gap-2 px-4 py-2 rounded ${
            mode === "searchEmail"
              ? "bg-indigo-600 text-white"
              : "bg-white text-gray-800 border hover:bg-gray-100"
          }`}
        >
          <FaSearch /> Buscar por Email
        </button>
      </div>

      {/* 🔍 Input de búsqueda */}
      {(mode === "searchId" || mode === "searchEmail") && (
        <div className="mb-6 flex gap-2">
          <input
            type="text"
            placeholder={
              mode === "searchId" ? "Ingrese ID de usuario..." : "Ingrese email de usuario..."
            }
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
            className="flex-1 border rounded px-3 py-2"
          />
          <button
            onClick={mode === "searchId" ? fetchUserById : fetchUserByEmail}
            className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 transition"
          >
            Buscar
          </button>
        </div>
      )}

      {/* 🧭 Vista principal */}
      {loading ? (
        <p>Cargando...</p>
      ) : error ? (
        <p className="text-red-500">{error}</p>
      ) : mode === "list" ? (
        <>
          {users.length === 0 ? (
            <p>No hay usuarios registrados.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {users.map((user) => (
                <div
                  key={user.id}
                  className="bg-white shadow rounded-lg p-4 flex justify-between items-center hover:shadow-lg transition"
                >
                  <div>
                    <h3 className="font-semibold text-lg text-indigo-600">{user.name}</h3>
                    <p className="text-gray-600">{user.email}</p>
              <p className="text-gray-500 text-sm">
                Rol: {formatRole(user.role)}
              </p>

                  </div>
                  <div className="flex gap-3">
                    <button
                      onClick={() => handleEdit(user.id)}
                      className="text-blue-600 hover:text-blue-800"
                    >
                      <FaEdit />
                    </button>
                    <button
                      onClick={() => handleDelete(user.id)}
                      className="text-red-600 hover:text-red-800"
                    >
                      <FaTrash />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
          {/* 📄 Paginación */}
          {users.length > 0 && (
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
      ) : singleUser ? (
        <div className="bg-white shadow rounded-lg p-4 flex justify-between items-center hover:shadow-lg transition">
          <div>
            <h3 className="font-semibold text-lg text-indigo-600">{singleUser.name}</h3>
            <p className="text-gray-600">{singleUser.email}</p>
          <p className="text-gray-500 text-sm">
            Rol: {formatRole(singleUser.role)}
          </p>

          </div>
          <div className="flex gap-3">
            <button
              onClick={() => handleEdit(singleUser.id)}
              className="text-blue-600 hover:text-blue-800"
            >
              <FaEdit />
            </button>
            <button
              onClick={() => handleDelete(singleUser.id)}
              className="text-red-600 hover:text-red-800"
            >
              <FaTrash />
            </button>
          </div>
        </div>
      ) : (
        <p className="text-gray-600">No se encontró ningún usuario</p>
      )}
    </div>
  );
}
