import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register as registerUser } from "../services/authService";
import { useAuth } from "../context/AuthContext";

export default function Register() {
  const navigate = useNavigate();
  const { login } = useAuth(); // ahora login llama al backend
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    role: "customer",
  });
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      setError("Las contraseñas no coinciden");
      return;
    }

    const mappedRole =
      formData.role === "customer" ? "ROLE_CUSTOMER" : "ROLE_PROVIDER";

    const payload = {
      name: formData.name,
      email: formData.email,
      password: formData.password,
      role: mappedRole,
    };

    try {
      // Registramos al usuario
      await registerUser(payload);

      // Login automático usando login real
      const userLogged = await login(formData.email, formData.password);

      // Redirigimos según rol
      if (userLogged.role === "ROLE_CUSTOMER") {
        navigate("/dashboard-customer");
      } else if (userLogged.role === "ROLE_PROVIDER") {
        navigate("/dashboard-provider");
      } else {
        navigate("/");
      }
    } catch (err) {
      setError(
        err.response?.data?.message || err.message || "Error al registrarse"
      );
    }
  };

  return (
    <div className="max-w-md mx-auto bg-white shadow-md rounded-lg p-6 mt-6">
      <h2 className="text-2xl font-bold text-center text-indigo-600 mb-4">
        Crear cuenta
      </h2>
      {error && <p className="text-red-500 text-center mb-4">{error}</p>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-gray-700">Nombre / Empresa</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2 mt-1"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700">Email</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2 mt-1"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700">Contraseña</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2 mt-1"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700">Confirmar Contraseña</label>
          <input
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2 mt-1"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700">Rol</label>
          <select
            name="role"
            value={formData.role}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2 mt-1"
          >
            <option value="customer">Cliente</option>
            <option value="provider">Proveedor</option>
          </select>
        </div>

        <button
          type="submit"
          className="w-full bg-indigo-600 text-white py-2 rounded hover:bg-indigo-700 transition"
        >
          Registrarse
        </button>
      </form>
    </div>
  );
}

