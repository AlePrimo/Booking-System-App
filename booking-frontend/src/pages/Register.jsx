import { useState } from "react";
import api from "../services/api";


export default function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/auth/register", { name, email, password });
      setMessage("Registro exitoso ✅");
    } catch {
      setMessage("Error al registrarse ❌");
    }
  };

  return (
    <div className="p-6 max-w-md mx-auto">
      <h2 className="text-2xl font-bold mb-4">Registro</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Nombre" className="w-full border p-2 rounded" />
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" className="w-full border p-2 rounded" />
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Contraseña" className="w-full border p-2 rounded" />
        <button type="submit" className="w-full bg-indigo-600 text-white p-2 rounded">Registrar</button>
      </form>
      {message && <p className="mt-4">{message}</p>}
    </div>
  );
}
