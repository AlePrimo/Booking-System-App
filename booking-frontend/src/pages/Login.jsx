import { useState } from "react";
import api from "../services/api";


export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/auth/login", { email, password });
      localStorage.setItem("token", res.data.token);
      setMessage("Login exitoso ✅");
    } catch {
      setMessage("Error al iniciar sesión ❌");
    }
  };

  return (
    <div className="p-6 max-w-md mx-auto">
      <h2 className="text-2xl font-bold mb-4">Login</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" className="w-full border p-2 rounded" />
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Contraseña" className="w-full border p-2 rounded" />
        <button type="submit" className="w-full bg-indigo-600 text-white p-2 rounded">Ingresar</button>
      </form>
      {message && <p className="mt-4">{message}</p>}
    </div>
  );
}
