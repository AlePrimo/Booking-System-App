import api from "./axiosClient";

export const register = (data) => api.post("/auth/register", data);

export const login = (data) => api.post("/auth/login", data);

export const refreshToken = (token) =>
  api.post("/auth/refresh", null, {
    headers: { Authorization: `Bearer ${token}` },
  });
