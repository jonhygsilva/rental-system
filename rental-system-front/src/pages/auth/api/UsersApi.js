import api from "../../../shared/api/axiosInstance";

const BASE = "/api/users";

export const loginUser = (email, password) =>
  api.post(`${BASE}/login`, { email, password });

export const registerUser = (data) =>
  api.post(`${BASE}/register`, data);
