import api from "./api";

export const getNotifications = (page = 0, size = 10) =>
  api.get(`/notifications?page=${page}&size=${size}`);

export const createNotification = (data) => api.post("/notifications", data);

export const deleteNotification = (id) => api.delete(`/notifications/${id}`);
