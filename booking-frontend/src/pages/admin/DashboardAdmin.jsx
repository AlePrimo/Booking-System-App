import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getUserById, getUserByEmail, getUsers, deleteUser } from "../../api/userService";
import { FaSearch, FaTrashAlt, FaEdit, FaPlus } from "react-icons/fa";

export default function DashboardAdmin() {
  const [searchValue, setSearchValue] = useState("");
  const [searchMode, setSearchMode] = useState("id");
  const [users, setUsers] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [page, setPage] = useState(0);
  const [selectedUser, setSelectedUser] = useState(null);
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    loadUsers();
  }, [page]);

  const loadUsers = async () => {
    try {
      const { data } = await getAllUsers(page, 10, token);
      setUsers(data.content);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error("Error al cargar usuarios", error);
    }
  };

  const handleSearch = async () => {
    if (!searchValue) return loadUsers();
    try {
      let res;
      if (searchMode === "id") {
        res = await getUserById(searchValue, token);
      } else {
        res = await getUserByEmail(searchValue, token);
      }
      setUsers(res.data ? [res.data] : []);
      setTotalPages(1);
      setPage(0);
    } catch (error) {
      console.error("Error al buscar usuario", error);
      setUsers([]);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm("¿Estás seguro de eliminar este usuario?")) return;
    try {
      await deleteUser(id, token);
      loadUsers();
    } catch (error) {
      console.error("Error al eliminar usuario", error);
    }
  };

  return (
    <div className="p-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-4xl font-bold text-indigo-600">👑 Panel de Administración</h1>
        <button
          onClick={() => navigate("/dashboard")}
          className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded transition"
        >
          ← Volver al Dashboard
        </button>
      </div>

      {/* Barra de búsqueda */}
      <div className="flex flex-col sm:flex-row gap-3 mb-6">
        <select
          value={searchMode}
          onChange={(e) => setSearchMode(e.target.value)}
          className="border rounded px-3 py-2"
        >
          <option value="id">Buscar por ID</option>
          <option value="email">Buscar por Email</option>
        </select>
        <input
          type="text"
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          placeholder={`Ingrese ${searchMode === "id" ? "ID" : "Email"}`}
          className="border rounded px-3 py-2 flex-1"
        />
        <button
          onClick={handleSearch}
          className="flex items-center gap-2 bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 transition"
        >
          <FaSearch /> Buscar
        </button>
        <button
          onClick={() => navigate("/admin/create-user")}
          className="flex items-center gap-2 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
        >
          <FaPlus /> Nuevo Usuario
        </button>
      </div>

      {/* Tabla de usuarios */}
      <div className="overflow-x-auto bg-white shadow rounded-lg">
        <table className="min-w-full text-sm text-gray-700">
          <thead className="bg-gray-100 text-left">
            <tr>
              <th className="px-4 py-2">ID</th>
              <th className="px-4 py-2">Nombre</th>
              <th className="px-4 py-2">Email</th>
              <th className="px-4 py-2">Roles</th>
              <th className="px-4 py-2 text-center">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {users.length > 0 ? (
              users.map((u) => (
                <tr key={u.id} className="border-b hover:bg-gray-50">
                  <td className="px-4 py-2">{u.id}</td>
                  <td className="px-4 py-2">{u.name}</td>
                  <td className="px-4 py-2">{u.email}</td>
                  <td className="px-4 py-2">{u.roles?.join(", ")}</td>
                  <td className="px-4 py-2 text-center space-x-2">
                    <button
                      onClick={() => navigate(`/admin/edit-user/${u.id}`)}
                      className="inline-flex items-center gap-1 px-2 py-1 bg-yellow-500 text-white rounded hover:bg-yellow-600 transition"
                    >
                      <FaEdit /> Editar
                    </button>
                    <button
                      onClick={() => handleDelete(u.id)}
                      className="inline-flex items-center gap-1 px-2 py-1 bg-red-600 text-white rounded hover:bg-red-700 transition"
                    >
                      <FaTrashAlt /> Eliminar
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5" className="text-center py-4 text-gray-500">
                  No se encontraron usuarios
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
