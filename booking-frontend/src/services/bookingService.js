import api from "./api";

export const getBookings = (page = 0, size = 10) =>
  api.get(`/bookings?page=${page}&size=${size}`);

export const getBookingById = (id) => api.get(`/bookings/${id}`);

export const createBooking = (data) => api.post("/bookings", data);

export const updateBooking = (id, data) => api.put(`/bookings/${id}`, data);

export const deleteBooking = (id) => api.delete(`/bookings/${id}`);
