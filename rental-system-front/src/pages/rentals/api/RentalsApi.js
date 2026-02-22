import api from '../../../shared/api/axiosInstance';

const BASE = '/api/rentals';

export const getRentals = (params) => api.get(BASE, { params });
export const getRental = (id) => api.get(`${BASE}/${id}`);
export const createRental = (payload) => api.post(BASE, payload);
export const updateRental = (id, payload) => api.put(`${BASE}/${id}`, payload);
export const deleteRental = (id) => api.delete(`${BASE}/${id}`);

export default {
  getRentals,
  getRental,
  createRental,
  updateRental,
  deleteRental,
};
