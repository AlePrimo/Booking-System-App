import api from "./axiosClient";

export const getBookings = (page = 0, size = 10, token) =>
  api.get(`/api/bookings?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const getBookingById = (id, token) =>
  api.get(`/api/bookings/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const createBooking = (data, token) =>
  api.post("/api/bookings", data, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const updateBookingStatus = (id, status, token) =>
  api.put(`/api/bookings/${id}/status?status=${status}`, null, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const deleteBooking = (id, token) =>
  api.delete(`/api/bookings/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
