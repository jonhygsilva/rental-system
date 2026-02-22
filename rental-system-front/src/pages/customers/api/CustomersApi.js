import api from "../../../shared/api/axiosInstance";

const BASE = "/api/customers";

export const getCustomers = ({ userId, page = 0, size = 10, sort = "name,asc", search = "", filters = {} }) => {
  const params = {
    userId,
    page,
    size,
    sort,
    search,
    ...filters
  };
  return api.get(BASE, { params });
};

export const getCustomer = (id, userId) =>
  api.get(`${BASE}/${id}`, { params: { userId } });

export const createCustomer = (data) =>
  api.post(BASE, data);

export const updateCustomer = (id, data) =>
  api.put(`${BASE}/${id}`, data);

export const deleteCustomer = (id, userId) =>
  api.delete(`${BASE}/${id}`, { params: { userId } });
