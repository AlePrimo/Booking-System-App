import { useNavigate } from "react-router-dom";

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/auth/login", { username, password });
      login(res.data);

      // Redirigir según rol
      if (res.data.role === "ROLE_ADMIN") {
        navigate("/dashboard-admin");
      } else {
        navigate("/dashboard");
      }
    } catch (err) {
      console.error(err);
    }
  };
}
