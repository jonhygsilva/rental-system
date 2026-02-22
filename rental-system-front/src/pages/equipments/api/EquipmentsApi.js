import api from "../../../shared/api/axiosInstance";

const BASE = "/api/equipments";

export const getEquipments = (userId) => api.get(BASE, { params: { userId } });
export const getEquipment = (id) => api.get(`${BASE}/${id}`);
export const createEquipment = (data) => api.post(BASE, data);
export const updateEquipment = (id, data) => api.put(`${BASE}/${id}`, data);
export const deleteEquipment = (id) => api.delete(`${BASE}/${id}`);
