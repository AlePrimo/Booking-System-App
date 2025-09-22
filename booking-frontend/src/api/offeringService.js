import api from "./axiosClient";

export const getOfferings = (page = 0, size = 10, token) =>
  api.get(`/api/offerings?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const getOfferingById = (id, token) =>
  api.get(`/api/offerings/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const createOffering = (data, token) =>
  api.post("/api/offerings", data, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const updateOffering = (id, data, token) =>
  api.put(`/api/offerings/${id}`, data, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const deleteOffering = (id, token) =>
  api.delete(`/api/offerings/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
