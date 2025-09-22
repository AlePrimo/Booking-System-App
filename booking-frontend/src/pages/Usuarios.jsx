import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import {
  getUsers,
  createUser,
  deleteUser,
} from "../api/userService";

export default function Usuarios() {
  const { token } = useAuth();
  const [users, setUsers] = useState([]);
  const [email, setEmail] = useState("");

  useEffect(() => {
    if (token) {
      getUsers(0, 10, token).then((res) => setUsers(res.data.content));
    }
  }, [token]);

  const handleCreate = async () => {
    const newUser = { email, password: "123456", name: "Nuevo" }; // 👈 ajustá DTO
    const res = await createUser(newUser, token);
    setUsers([...users, res.data]);
    setEmail("");
  };

  const handleDelete = async (id) => {
    await deleteUser(id, token);
    setUsers(users.filter((u) => u.id !== id));
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Usuarios</h2>
      <div className="flex mb-4">
        <input
          type="email"
          placeholder="Nuevo email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
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
        {users.map((u) => (
          <li key={u.id} className="flex justify-between items-center mb-2">
            {u.email}
            <button
              onClick={() => handleDelete(u.id)}
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
