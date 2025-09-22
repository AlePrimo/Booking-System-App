import api from "./api";

export const getPayments = (page = 0, size = 10) =>
  api.get(`/payments?page=${page}&size=${size}`);

export const getPaymentById = (id) => api.get(`/payments/${id}`);

export const createPayment = (data) => api.post("/payments", data);

export const deletePayment = (id) => api.delete(`/payments/${id}`);
