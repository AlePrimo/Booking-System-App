import api from "./api";

export const register = (data) => api.post("/auth/register", data);
export const login = (data) => api.post("/auth/login", data);
export const refreshToken = (data) => api.post("/auth/refresh", data);
