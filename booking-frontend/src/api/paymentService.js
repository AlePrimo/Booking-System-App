import api from "./axiosClient";

export const getPayments = (page = 0, size = 10, token) =>
  api.get(`/api/payments?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const getPaymentById = (id, token) =>
  api.get(`/api/payments/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const createPayment = (data, token) =>
  api.post("/api/payments", data, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const updatePaymentStatus = (id, status, token) =>
  api.put(`/api/payments/${id}/status?status=${status}`, null, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const deletePayment = (id, token) =>
  api.delete(`/api/payments/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
