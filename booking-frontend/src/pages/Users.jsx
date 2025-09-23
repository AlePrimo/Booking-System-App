import { useEffect, useState } from "react";
import api from "../services/api";


export default function Users() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    api.get("/users").then((res) => setUsers(res.data.content || res.data));
  }, []);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Usuarios</h2>
      <ul className="space-y-2">
        {users.map((u) => (
          <li key={u.id} className="border p-2 rounded">
            {u.name} - {u.email}
          </li>
        ))}
      </ul>
    </div>
  );
}
