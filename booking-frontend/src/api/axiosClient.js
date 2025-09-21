import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api", // 👈 cambia si tu backend corre en otro puerto/ruta
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;
