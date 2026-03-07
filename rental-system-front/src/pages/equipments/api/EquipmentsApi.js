import api from "../../../shared/api/axiosInstance";

const BASE = "/api/equipments";

export const getEquipments = ({ page = 0, size = 10, sort = "name,asc", search = "" } = {}) => {
  const params = { page, size, sort, search };
  return api.get(BASE, { params });
};
export const getEquipment = (id) => api.get(`${BASE}/${id}`);
export const createEquipment = (data) => api.post(BASE, data);
export const updateEquipment = (id, data) => api.put(`${BASE}/${id}`, data);
export const deleteEquipment = (id) => api.delete(`${BASE}/${id}`);
