import { useNavigate } from "react-router-dom";

export default function Register() {
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await api.post("/auth/register", { username, password, email });
      alert("Registro exitoso, ahora puedes iniciar sesión");
      navigate("/login");
    } catch (err) {
      console.error(err);
    }
  };
}