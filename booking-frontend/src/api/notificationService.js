import api from "./axiosClient";

export const getNotifications = (page = 0, size = 10, token) =>
  api.get(`/api/notifications?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const getNotificationById = (id, token) =>
  api.get(`/api/notifications/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const createNotification = (data, token) =>
  api.post("/api/notifications", data, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const markAsRead = (id, token) =>
  api.put(`/api/notifications/${id}/read`, null, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const deleteNotification = (id, token) =>
  api.delete(`/api/notifications/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
