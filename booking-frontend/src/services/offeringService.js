import api from "./api";

export const getOfferings = (page = 0, size = 10) =>
  api.get(`/offerings?page=${page}&size=${size}`);

export const getOfferingById = (id) => api.get(`/offerings/${id}`);

export const createOffering = (data) => api.post("/offerings", data);

export const updateOffering = (id, data) => api.put(`/offerings/${id}`, data);

export const deleteOffering = (id) => api.delete(`/offerings/${id}`);
