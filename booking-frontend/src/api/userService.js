import api from "./axiosClient";

export const getUsers = (page = 0, size = 10, token) =>
  api.get(`/api/users?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const getUserById = (id, token) =>
  api.get(`/api/users/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const updateUser = (id, data, token) =>
  api.put(`/api/users/${id}`, data, {
    headers: { Authorization: `Bearer ${token}` },
  });

export const deleteUser = (id, token) =>
  api.delete(`/api/users/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
export const getUserByEmail = (email, token) =>
  api.get(`/api/users/email/${email}`, {
    headers: { Authorization: `Bearer ${token}` },
  });